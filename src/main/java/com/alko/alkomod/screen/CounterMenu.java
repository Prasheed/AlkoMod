package com.alko.alkomod.screen;

import com.alko.alkomod.capability.ModCapabilitiesRegister;
import com.alko.alkomod.util.ICountData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;


public class CounterMenu extends AbstractContainerMenu {
    private final ItemStack stack;
    private final ContainerData data; // Используем ContainerData для синхронизации

    public CounterMenu(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        this(id, playerInv, buffer.readItem());
    }

    public CounterMenu(int id, Inventory playerInv, ItemStack stack) {
        super(ModMenuTypes.COUNTER_MENU.get(), id);
        this.stack = stack;

        this.data = new ContainerData() {
            private int countValue = 0;
            private int someOtherValue = 0; // Можно передавать дополнительные данные

            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> countValue;
                    case 1 -> someOtherValue;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> countValue = value;
                    case 1 -> someOtherValue = value;
                }
            }

            @Override
            public int getCount() {
                return 2; // Количество значений, которые мы передаём
            }
        };

        // Загружаем данные из Capability
        stack.getCapability(ModCapabilitiesRegister.COUNT_DATA).ifPresent(data -> this.data.set(0, data.getCount()));

        addDataSlots(this.data); // Добавляем синхронизацию
    }

    public int getCountValue() {
        return this.data.get(0);
    }

    public int getSomeOtherValue() {
        return this.data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return !stack.isEmpty();
    }

}
