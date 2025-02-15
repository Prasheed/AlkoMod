package com.alko.alkomod.energy;

public interface IBeerEnergyStorageBlock {
    // Методы для получения значений
    int getCapacity();
    int getMaxExtract();
    int getMaxReceive();
    int getStoredEnergy();

    // Методы для управления энергией
    int receiveEnergy(int amount, boolean simulate);
    int extractEnergy(int amount, boolean simulate);

    // Методы для проверки возможностей
    boolean canReceive();
    boolean canExtract();
    boolean isFull();
}
