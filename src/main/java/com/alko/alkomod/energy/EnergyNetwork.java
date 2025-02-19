package com.alko.alkomod.energy;

import com.alko.alkomod.block.blockentity.BEBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.function.Consumer;

public class EnergyNetwork {

    private final int networkId;
    private final HashSet<BEBlockEntity> inputBlocks;  // Блоки с INPUT
    private final HashSet<BEBlockEntity> outputBlocks;

    public EnergyNetwork(HashSet<BEBlockEntity> inputBlocks, HashSet<BEBlockEntity> outputBlocks, int networkId) {
        this.inputBlocks = inputBlocks;
        this.outputBlocks = outputBlocks;
        this.networkId = networkId;
        //
        if(!this.outputBlocks.isEmpty()){
            highlightMembers(this.outputBlocks.iterator().next().getLevel());
        }else if (!this.inputBlocks.isEmpty()){
            highlightMembers(this.inputBlocks.iterator().next().getLevel());
        }
        updateInputs();
    }

    public HashSet<BEBlockEntity> getInputBlocks() {
        return inputBlocks;
    }

    public HashSet<BEBlockEntity> getOutputBlocks() {
        return outputBlocks;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void addInputBlock(BEBlockEntity blockEntity) {
        if (this.inputBlocks.add(blockEntity)) { // Добавит только если нет в списке
            updateInputs();
            highlightMembers(blockEntity.getLevel());
        }
    }

    public void addOutputBlock(BEBlockEntity blockEntity) {
        if (this.outputBlocks.add(blockEntity)) {
            updateInputs();
            highlightMembers(blockEntity.getLevel());
        }
    }

    public void delInputBlock(BEBlockEntity blockEntity) {
        if (this.inputBlocks.remove(blockEntity)) {  // Проверка, был ли блок в списке
            updateInputs();
            highlightMembers(blockEntity.getLevel());
            System.out.println("Удалён блок с INPUT: " + blockEntity.getBlockPos());
        } else {
            System.out.println("Не удалось удалить блок с INPUT: " + blockEntity.getBlockPos());
        }
    }

    public void delOutputBlock(BEBlockEntity blockEntity) {
        if (this.outputBlocks.remove(blockEntity)) {
            updateInputs();
            highlightMembers(blockEntity.getLevel());
            System.out.println("Удалён блок с OUTPUT: " + blockEntity.getBlockPos());
        } else {
            System.out.println("Не удалось удалить блок с OUTPUT: " + blockEntity.getBlockPos());
        }
    }

    public void highlightMembers(Level level){
        if (level instanceof ServerLevel serverLevel) {
            // Параметры частиц
            ParticleOptions inputParticle = ParticleTypes.END_ROD;     // Яркая и заметная частица
            ParticleOptions outputParticle = ParticleTypes.FLAME;      // Контрастная частица

            // Функция для отображения частиц на позиции блока
            Consumer<BEBlockEntity> spawnParticles = (beBlock) -> {
                BlockPos pos = beBlock.getBlockPos();
                double x = pos.getX() + 0.5;
                double y = pos.getY() + 1.0;  // Чуть выше блока для видимости
                double z = pos.getZ() + 0.5;

                ParticleOptions particle = inputBlocks.contains(beBlock) ? inputParticle : outputParticle;

                serverLevel.sendParticles(
                        particle,    // Тип частиц
                        x, y, z,     // Позиция
                        30,          // Кол-во частиц
                        0.3, 0.5, 0.3, // Смещение по X, Y, Z
                        0.0001         // Скорость частицы
                );
            };

            // 🔋 INPUT блоки — END_ROD частицы
            inputBlocks.forEach(spawnParticles);

            // ⚡ OUTPUT блоки — FLAME частицы
            outputBlocks.forEach(spawnParticles);

            System.out.println("Сеть под Id "+this.networkId+" Output= "+this.outputBlocks.size()+" Input= "+this.inputBlocks.size());
        }
    }

    public void updateInputs(){
        outputBlocks.forEach(blockEntity -> {
            blockEntity.getInputs().clear();
            blockEntity.getInputs().addAll(inputBlocks);
        });
    }
}
