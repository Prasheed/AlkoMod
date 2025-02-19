package com.alko.alkomod.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerData extends ItemStackHandler implements IItemStackHandlerData {
    private int storedEnergy; // Число, которое нужно хранить

    public ItemStackHandlerData(int size) {
        super(size);
    }

    public int getStoredValue() {
        return storedEnergy;
    }

    public void setStoredEnergy(int value) {
        this.storedEnergy = value;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putInt("StoredValue", storedEnergy); // Сохраняем число
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.storedEnergy = nbt.getInt("StoredValue"); // Загружаем число
    }

    @Override
    public ItemStackHandler getHandler() {
        return this;
    }
}
