package com.alko.alkomod.energy;

import com.alko.alkomod.block.CableBlock;
import com.alko.alkomod.block.ModBlocks;
import com.alko.alkomod.block.blockentity.BEBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class EnergySystemUtils {



    public static Map<String, HashSet<BlockPos>> buildNetwork(BlockPos startPos, Level level) {
        HashSet<BlockPos> inputBlocks = new HashSet<>();  // Блоки с INPUT
        HashSet<BlockPos> outputBlocks = new HashSet<>(); // Блоки с OUTPUT
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            BlockState state = level.getBlockState(currentPos);

            System.out.println("🔍 Проверяем блок: " + currentPos + " -> " + state.getBlock());

            // Проверяем, является ли текущий блок энергоблоком
            BlockEntity blockEntity = level.getBlockEntity(currentPos);
            if (blockEntity instanceof BEBlockEntity beBlockEntity) {
                System.out.println("⚡ Найден энергоблок: " + currentPos);

                boolean hasInput = false;
                boolean hasOutput = false;

                for (Direction dir : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(dir);
                    BlockState neighborState = level.getBlockState(neighborPos);

                    System.out.println("  ➜ Проверяем сторону " + dir + ": " + neighborPos + " -> " + neighborState.getBlock());

                    if (neighborState.getBlock() instanceof CableBlock) {
                        EnergySide sideMode = beBlockEntity.getSideMode(dir);
                        System.out.println("    🔄 Конфигурация стороны " + dir + ": " + sideMode);

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
                    inputBlocks.add(currentPos);
                    System.out.println("    ✅ Добавляем в INPUT список: " + currentPos);
                }
                if (hasOutput) {
                    outputBlocks.add(currentPos);
                    System.out.println("    ✅ Добавляем в OUTPUT список: " + currentPos);
                }

                if (!hasInput && !hasOutput) {
                    System.out.println("❌ Энергоблок не имеет подключения к проводу, пропускаем.");
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
                        System.out.println("  🔗 Добавляем в очередь: " + neighborPos);
                    }
                }
            }
        }

        // Возвращаем оба списка в виде Map
        Map<String, HashSet<BlockPos>> result = new HashMap<>();
        result.put("INPUT", inputBlocks);
        result.put("OUTPUT", outputBlocks);
        return result;
    }

    private static boolean isEnergyBlock(BlockPos pos, Level level) {
        BlockEntity entity = level.getBlockEntity(pos);
        return entity instanceof BEBlockEntity;
    }
}
