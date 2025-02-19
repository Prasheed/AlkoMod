package com.alko.alkomod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerData extends ItemStackHandler implements IItemStackHandlerData {
    public ItemStackHandlerData(int size) {
        super(size);
    }

    @Override
    public CompoundTag serializeNBT() {
        return super.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    @Override
    public ItemStackHandler getHandler() {
        return this;
    }
}
