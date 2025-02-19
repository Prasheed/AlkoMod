package com.alko.alkomod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public interface ICountData {

    int getCount();

    void setCount(int count);

    void increment();

    Tag serializeNBT();

    void deserializeNBT(CompoundTag countData);
}