package com.alko.alkomod.screen;

import com.alko.alkomod.capability.ModCapabilitiesRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CounterMenu extends AbstractContainerMenu {
    private final ItemStack stack;
    private final ItemStackHandler inventory;

    public CounterMenu(int id, Inventory playerInv, FriendlyByteBuf buffer) {
        this(id, playerInv, buffer.readItem());
    }

    public CounterMenu(int id, Inventory playerInv, ItemStack stack) {
        super(ModMenuTypes.COUNTER_MENU.get(), id);
        this.stack = stack;

        this.inventory = new ItemStackHandler(15) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);

                // Сохраняем инвентарь в предмет
                stack.getCapability(ModCapabilitiesRegister.INVENTORY_DATA).ifPresent(data -> {
                    data.getHandler().setStackInSlot(slot, getStackInSlot(slot));
                });
            }
        };
        stack.getCapability(ModCapabilitiesRegister.INVENTORY_DATA).ifPresent(data -> {
            for (int i = 0; i < inventory.getSlots(); i++) {
                inventory.setStackInSlot(i, data.getHandler().getStackInSlot(i));
            }
        });

        // Добавление слотов инвентаря предмета (5 столбцов x 3 строки)
        int startX = 8 + 18 + 18; // Второй слот начинается на 8 + 18
        int startY = 18; // Начальная высота
        int slotIndex = 0;

        for (int row = 0; row < 3; row++) { // 3 строки
            for (int col = 0; col < 5; col++) { // 5 слотов в ряд
                addSlot(new SlotItemHandler(inventory, slotIndex++, startX + col * 18, startY + row * 18));
            }
        }

        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return !stack.isEmpty();
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
