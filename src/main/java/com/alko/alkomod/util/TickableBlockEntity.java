package com.alko.alkomod.util;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;

public interface TickableBlockEntity {

    void tick();

    static <T extends BlockEntity>BlockEntityTicker<T> getTickerHelper(Level level){
        return level.isClientSide() ? null : (plevel, blockPos, blockState, blockEntity) -> ((TickableBlockEntity)blockEntity).tick();
    }
}
