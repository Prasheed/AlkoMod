package com.alko.alkomod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemStackHandler;

public interface IItemStackHandlerData extends INBTSerializable<CompoundTag> {
    ItemStackHandler getHandler();
}