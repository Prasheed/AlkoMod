package com.alko.alkomod.energy;

import net.minecraft.world.item.ItemStack;

public interface IBeerEnergyStorageItem {
    int receiveEnergy(ItemStack stack, int amount, boolean simulate);

    // Извлекает энергию
    int extractEnergy(ItemStack stack, int amount, boolean simulate);

    // Получает текущий заряд энергии
    int getStoredEnergy(ItemStack stack);

    // Получает максимальную ёмкость для хранения энергии
    int getMaxEnergy(ItemStack stack);

    // Может ли объект принимать энергию?
    boolean canReceive();

    // Может ли объект отдавать энергию?
    boolean canExtract();
}
