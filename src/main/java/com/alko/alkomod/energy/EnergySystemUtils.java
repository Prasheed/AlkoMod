package com.alko.alkomod.energy;

import com.alko.alkomod.block.CableBlock;
import com.alko.alkomod.block.blockentity.BEBlockEntity;
import com.alko.alkomod.block.blockentity.CableBlockBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class EnergySystemUtils {

    public static void addCablesToNetwork(BlockPos startPos, Level level, int networkId){
        AtomicInteger count = new AtomicInteger();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);
        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            BlockState state = level.getBlockState(currentPos);
            BlockEntity blockEntity = level.getBlockEntity(currentPos);
            if(blockEntity instanceof CableBlockBlockEntity){
                for (Direction direction : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(direction);
                    if (visited.contains(neighborPos)) continue;

                    BlockEntity be = level.getBlockEntity(neighborPos);
                    if(be instanceof CableBlockBlockEntity cableEntity && cableEntity.getNetworkId() == -1){
                        queue.add(neighborPos);
                        visited.add(neighborPos);
                    }

                }
            }
        }

        visited.forEach(blockPos -> {

            if(level.getBlockState(blockPos).hasBlockEntity() && level.getBlockEntity(blockPos) instanceof CableBlockBlockEntity cableEntity){
                if(cableEntity.getNetworkId() == -1){
                    cableEntity.setNetworkId(networkId);
                    count.getAndIncrement();
                }
            }

        });
        System.out.println("Добавлено к сети под Id "+ networkId+"кабелей "+count);
    }

    public static Map<String, HashSet<BEBlockEntity>> buildNetwork(BlockPos startPos, Level level, boolean rebuild) {
        int newId = EnergyNetworkList.getFreeId();
        HashSet<BEBlockEntity> inputBlocks = new HashSet<>();  // Блоки с INPUT
        HashSet<BEBlockEntity> outputBlocks = new HashSet<>(); // Блоки с OUTPUT
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            BlockState state = level.getBlockState(currentPos);

            // Проверяем, является ли текущий блок энергоблоком
            BlockEntity blockEntity = level.getBlockEntity(currentPos);
            if (blockEntity instanceof BEBlockEntity beBlockEntity) {

                boolean hasInput = false;
                boolean hasOutput = false;

                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(dir);
                    BlockState neighborState = level.getBlockState(neighborPos);

                    if (neighborState.getBlock() instanceof CableBlock) {
                        EnergySide sideMode = beBlockEntity.getSideMode(dir);

                        if (sideMode == EnergySide.INPUT) {
                            hasInput = true;
                        }
                        if (sideMode == EnergySide.OUTPUT) {
                            hasOutput = true;
                        }
                    }
                }

                // Добавляем в соответствующие списки
                if (hasInput) {
                    if(level.getBlockEntity(currentPos) instanceof BEBlockEntity be){
                        inputBlocks.add(be);
                    }

                }
                if (hasOutput) {
                    if(level.getBlockEntity(currentPos) instanceof BEBlockEntity be){
                        outputBlocks.add(be);
                    }
                }
                continue;
            }

            // Если это кабель, продолжаем BFS
            if (state.getBlock() instanceof CableBlock) {
                for (Direction direction : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(direction);
                    if (visited.contains(neighborPos)) continue;

                    BlockState neighborState = level.getBlockState(neighborPos);
                    if (neighborState.getBlock() instanceof CableBlock || isEnergyBlock(neighborPos, level)) {
                        queue.add(neighborPos);
                        visited.add(neighborPos);
                    }
                }
            }
        }

        // Возвращаем оба списка в виде Map
        Map<String, HashSet<BEBlockEntity>> result = new HashMap<>();

        result.put("INPUT", inputBlocks);
        result.put("OUTPUT", outputBlocks);
        if(!rebuild){
            if(!inputBlocks.isEmpty() || !outputBlocks.isEmpty()){
                visited.forEach(blockPos -> {
                    tryChangeCableNetworkId(blockPos,level,newId);
                });
            }
        }
        return result;
    }

    private static boolean isEnergyBlock(BlockPos pos, Level level) {
        BlockEntity entity = level.getBlockEntity(pos);
        return entity instanceof BEBlockEntity;
    }

    private static void tryChangeCableNetworkId(BlockPos pos, Level level, int newId){
        if(level.getBlockState(pos).hasBlockEntity()){
            if(level.getBlockEntity(pos) instanceof CableBlockBlockEntity be){
                be.setNetworkId(newId);
            }
        }
    }

    public static void rebuildNetworkById(CableBlockBlockEntity cableEntity){
        int networkId = cableEntity.getNetworkId();
        BlockPos pos = cableEntity.getBlockPos();
        if(networkId!=-1){
            EnergyNetwork network = EnergyNetworkList.getNetworkById(networkId);
            Map<String, HashSet<BEBlockEntity>> inouts = buildNetwork(pos, cableEntity.getLevel(), true);

            network.getInputBlocks().clear();
            network.getOutputBlocks().clear();
            network.getInputBlocks().addAll(inouts.get("INPUT"));
            network.getOutputBlocks().addAll(inouts.get("OUTPUT"));
            network.updateInputs();
        }
    }
}
