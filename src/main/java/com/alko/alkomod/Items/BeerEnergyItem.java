package com.alko.alkomod.Items;

import com.alko.alkomod.energy.IBeerEnergyStorageItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BeerEnergyItem extends Item implements IBeerEnergyStorageItem {

    protected final int capacity;
    protected final int maxReceive;
    protected final int maxExtract;

    public BeerEnergyItem(Properties pProperties, int capacity, int maxReceive, int maxExtract) {
        super(pProperties);
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public int getStoredEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt("Energy");
    }

    // Установка хранимой энергии
    public static void setStoredEnergy(ItemStack stack, int energy) {
        stack.getOrCreateTag().putInt("Energy", Math.min(energy, ((BeerEnergyItem) stack.getItem()).capacity));
    }

    @Override
    public int receiveEnergy(ItemStack stack, int amount, boolean simulate) {
        int energy = getStoredEnergy(stack);
        int accepted = Math.min(capacity - energy, Math.min(amount, maxReceive));

        if (!simulate) {
            setStoredEnergy(stack, energy + accepted);
        }

        return accepted;
    }

    @Override
    public int extractEnergy(ItemStack stack, int amount, boolean simulate) {
        int energy = getStoredEnergy(stack);
        int extracted = Math.min(energy, Math.min(amount, maxExtract));

        if (!simulate) {
            setStoredEnergy(stack, energy - extracted);
        }

        return extracted;
    }


    @Override
    public int getMaxEnergy(ItemStack stack) {
        return this.capacity;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
    }
}
