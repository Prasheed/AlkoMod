package com.alko.alkomod.energy;

public interface IBeerEnergyStorageBlock {
    int receiveEnergy(int amount, boolean simulate);

    // Извлекает энергию
    int extractEnergy(int amount, boolean simulate);

    // Получает текущий заряд энергии
    int getStoredEnergy();

    // Получает максимальную ёмкость для хранения энергии
    int getMaxEnergy();

    // Может ли объект принимать энергию?
    boolean canReceive();

    // Может ли объект отдавать энергию?
    boolean canExtract();
}
