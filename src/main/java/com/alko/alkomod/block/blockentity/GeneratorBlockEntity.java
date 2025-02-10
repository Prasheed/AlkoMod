package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GeneratorBlockEntity extends BlockEntity {

    public boolean workState = false;

    public GeneratorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GENERATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        CompoundTag AlkoModData = tag.getCompound(Alkomod.MOD_ID);
        this.workState = AlkoModData.getBoolean("work_state");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        var AlkoModData = new CompoundTag();
        AlkoModData.putBoolean("work_state", this.workState);
        tag.put(Alkomod.MOD_ID,AlkoModData);

    }
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    public boolean getWorkState() {
        return workState;
    }

    public boolean changeWorkState(){
        workState = !workState;
        return workState;
    }
}
