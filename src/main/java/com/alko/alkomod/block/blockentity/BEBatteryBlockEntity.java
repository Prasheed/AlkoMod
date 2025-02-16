package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.energy.IBeerEnergyStorageItem;
import com.alko.alkomod.screen.BEBatteryMenu;
import com.alko.alkomod.util.EnergyUtils;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxUI;

public class BEBatteryBlockEntity extends BEBlockEntity implements MenuProvider, TickableBlockEntity {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.getItem() instanceof IBeerEnergyStorageItem;
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;

    public BEBatteryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.BE_BATTERY_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.capacity = 5000;
        this.maxReceive = 20;
        this.maxExtract = 20;
        this.storedEnergy = 0;
        this.data = new ContainerData(){

            @Override
            public int get(int i) {
                switch (i){
                    case 0 ->{
                        return BEBatteryBlockEntity.this.capacity;
                    }
                    case 1 ->{
                        return BEBatteryBlockEntity.this.storedEnergy;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            @Override
            public void set(int i, int i1) {
                switch (i){
                    case 1 -> BEBatteryBlockEntity.this.storedEnergy = i1;
                    case 0 -> BEBatteryBlockEntity.this.capacity = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("пивточка");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BEBatteryMenu(i, inventory,this, this.data);
    }

    @Override
    public void tick() {
        chargeIfCan();
        dischargeIfCan();
    }

    private void dischargeIfCan() {
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (stack.getItem() instanceof IBeerEnergyStorageItem item){
            if (!stack.isEmpty()){
                if (item.getStoredEnergy(stack) > 0 && this.getCapacity() != this.getStoredEnergy()){
                    EnergyUtils.transferMaxPossibleEnergy(item,stack,this);
                }
            }
        }

    }

    private void chargeIfCan() {
        ItemStack stack = itemHandler.getStackInSlot(1);
        if (stack.getItem() instanceof IBeerEnergyStorageItem item){
            if (!stack.isEmpty()){
                if (this.getStoredEnergy() > 0 && item.getStoredEnergy(stack) != item.getMaxEnergy(stack)){
                    EnergyUtils.transferMaxPossibleEnergy(this, item, stack);
                }
            }
        }
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag tag = pTag.getCompound(Alkomod.MOD_ID);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        CompoundTag tag = new CompoundTag();
        tag.put("inventory", itemHandler.serializeNBT());
        pTag.put(Alkomod.MOD_ID, tag);
        super.saveAdditional(pTag);
    }

}
