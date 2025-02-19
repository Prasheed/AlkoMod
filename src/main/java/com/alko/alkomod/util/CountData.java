package com.alko.alkomod.util;

import net.minecraft.nbt.CompoundTag;

public class CountData implements ICountData {
    private int count = 0; // Изначальное значение счётчика

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void increment() {
        this.count++; // Увеличиваем число
    }
    // Сериализация данных
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("count", this.count);
        return tag;
    }

    // Десериализация данных
    public void deserializeNBT(CompoundTag tag) {
        this.count = tag.getInt("count");
    }
}
