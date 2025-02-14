package com.alko.alkomod.util;

import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.energy.IBeerEnergyStorageItem;
import net.minecraft.world.item.ItemStack;

public class EnergyUtils {

    // 1. Передача энергии от блока к блоку
    public static int transferEnergy(IBeerEnergyStorageBlock source, IBeerEnergyStorageBlock target, int amount) {
        int extracted = source.extractEnergy(amount, true);
        int received = target.receiveEnergy(extracted, true);

        source.extractEnergy(received, false);
        target.receiveEnergy(received, false);

        return amount - received;
    }

    // 2. Передача энергии от блока к предмету
    public static int transferEnergy(IBeerEnergyStorageBlock source, ItemStack item, IBeerEnergyStorageItem target, int amount) {
        int extracted = source.extractEnergy(amount, true);
        int received = target.receiveEnergy(item, extracted, true);

        source.extractEnergy(received, false);
        target.receiveEnergy(item, received, false);

        return amount - received;
    }

    // 3. Передача энергии от предмета к блоку
    public static int transferEnergy(ItemStack item, IBeerEnergyStorageItem source, IBeerEnergyStorageBlock target, int amount) {
        int extracted = source.extractEnergy(item, amount, true);
        int received = target.receiveEnergy(extracted, true);

        source.extractEnergy(item, received, false);
        target.receiveEnergy(received, false);

        return amount - received;
    }

    // 4. Передача энергии между предметами
    public static int transferEnergy(ItemStack sourceItem, IBeerEnergyStorageItem source, ItemStack targetItem, IBeerEnergyStorageItem target, int amount) {
        int extracted = source.extractEnergy(sourceItem, amount, true);
        int received = target.receiveEnergy(targetItem, extracted, true);

        source.extractEnergy(sourceItem, received, false);
        target.receiveEnergy(targetItem, received, false);

        return amount - received;
    }
}
