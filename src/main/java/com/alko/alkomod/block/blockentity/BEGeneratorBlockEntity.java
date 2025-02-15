package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.energy.IBeerEnergyStorageItem;
import com.alko.alkomod.screen.BEGeneratorBlockMenu;
import com.alko.alkomod.util.EnergyUtils;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BEGeneratorBlockEntity extends BEBlockEntity implements TickableBlockEntity, MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3){
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            switch (slot){
                case 0 -> {
                    return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
                }
                case 1, 2 -> {
                    return stack.getItem() instanceof IBeerEnergyStorageItem;
                }
            }
            return false;
        }
    };
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    protected final ContainerData data;
    private int ticks = 0;
    private int litTime;
    private int litDuration;


    public BEGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.BE_GENERATOR_BLOCK_ENTITY.get(), pos, state);
        this.capacity = 10000;
        this.maxExtract = 10;
        this.maxReceive = 10;
        this.storedEnergy = 0;
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                switch (i) {
                    case 0 -> {
                        return BEGeneratorBlockEntity.this.litTime;
                    }
                    case 1 -> {
                        return BEGeneratorBlockEntity.this.litDuration;
                    }
                    case 2 ->{
                        return BEGeneratorBlockEntity.this.storedEnergy;
                    }
                    case 3 ->{
                        return BEGeneratorBlockEntity.this.capacity;
                    }
                    default -> {
                        return 0;
                    }
                }
            }

            @Override
            public void set(int i, int i1) {
                    switch (i) {
                        case 0 -> BEGeneratorBlockEntity.this.litTime = i1;
                        case 1 -> BEGeneratorBlockEntity.this.litDuration = i1;
                        case 2 -> BEGeneratorBlockEntity.this.storedEnergy = i1;
                        case 3 -> BEGeneratorBlockEntity.this.capacity = i1;
                    }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for(int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER){
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(()-> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag =  super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag data = pTag.getCompound(Alkomod.MOD_ID);
        this.litTime = data.getInt("litTime");
        this.litDuration = data.getInt("litDuration");
        itemHandler.deserializeNBT(data.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        var data = new CompoundTag();
        data.put("inventory", itemHandler.serializeNBT());
        data.putInt("litTime", this.litTime);
        data.putInt("litDuration", this.litDuration);
        pTag.put(Alkomod.MOD_ID, data);
        super.saveAdditional(pTag);
    }

    public void test(Player player){
        player.sendSystemMessage(Component.literal("блок энтити работает"));

        if (player.level().isClientSide()){
            player.sendSystemMessage(Component.literal(String.valueOf(getStoredEnergy())+" на клиенте"));
        }else{
            player.sendSystemMessage(Component.literal(String.valueOf(getStoredEnergy())+" на сервере"));
        }
    }

    @Override
    public void tick() {
        if(this.isLit()){
            this.litTime--;
            setChanged();
            receiveEnergy(5,false);
        }
        if(hasFuel() && !this.isLit() && getStoredEnergy() != getCapacity()){
            decreaseFuelAndLit();
        }
        chargeItemIfCan();
        dischargeItemIfCan();

        ticks++;
    }

    private void dischargeItemIfCan() {
        ItemStack stack = itemHandler.getStackInSlot(2);
        if(stack.getItem() instanceof IBeerEnergyStorageItem storageItem){
            if(this.getStoredEnergy()<this.getCapacity() && storageItem.getStoredEnergy(stack)>0){
                EnergyUtils.transferMaxPossibleEnergy(storageItem,stack,this);
            }
        }
    }

    private void chargeItemIfCan() {
        ItemStack stack = itemHandler.getStackInSlot(1);
        if(stack.getItem() instanceof IBeerEnergyStorageItem storageItem){
            if(this.getStoredEnergy() > 0 && storageItem.getStoredEnergy(stack) < storageItem.getMaxEnergy(stack)){
                EnergyUtils.transferMaxPossibleEnergy(this, storageItem, stack);
            }
        }
    }

    private void decreaseFuelAndLit() {
        ItemStack stack = itemHandler.getStackInSlot(0);
        int burnTime = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) / 3;
        itemHandler.extractItem(0,1,false);
        this.litDuration = burnTime;
        this.litTime = this.litDuration;
    }

    private boolean hasFuel() {
        return !itemHandler.getStackInSlot(0).isEmpty();
    }

    private boolean isLit() {
        return litTime > 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Генератор BE энергии");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new BEGeneratorBlockMenu(i, inventory, this, this.data);
    }
}
