package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleEnergyGeneratorBlockEntity extends BlockEntity {

    private int counter;

    public SimpleEnergyGeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.SIMPLE_GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag AlkoModData = tag.getCompound(Alkomod.MOD_ID);
        this.counter = AlkoModData.getInt("counter");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        var AlkoModData = new CompoundTag();
        AlkoModData.putInt("counter", this.counter);
        tag.put(Alkomod.MOD_ID,AlkoModData);
    }

    public int increaseCounter() {
        ++this.counter;
        setChanged();
        return this.counter;
    }

    public int getCounter(){
        return this.counter;
    }
}
