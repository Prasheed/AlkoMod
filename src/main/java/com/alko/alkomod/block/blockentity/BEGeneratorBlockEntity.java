package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import com.alko.alkomod.screen.BEGeneratorBlockMenu;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
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

public class BEGeneratorBlockEntity extends BlockEntity implements IBeerEnergyStorageBlock, TickableBlockEntity, MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    private LazyOptional<IItemHandler> lazyItemHadler = LazyOptional.empty();
    protected final ContainerData data;
    private int energyStored;
    private int capacity = 10000;
    private final int maxReceive = 5;
    private int maxExtract = 5;
    private int ticks = 0;
    private int litTime;
    private int litDuration;


    public BEGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntity.GENERATOR_BLOCK_ENTITY.get(), pos, state);
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
                        return BEGeneratorBlockEntity.this.energyStored;
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
                        case 2 -> BEGeneratorBlockEntity.this.energyStored = i1;
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
            return lazyItemHadler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHadler = LazyOptional.of(()-> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHadler.invalidate();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag =  super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag data = pTag.getCompound(Alkomod.MOD_ID);
        this.energyStored = data.getInt("energyStored");
        this.litTime = data.getInt("litTime");
        itemHandler.deserializeNBT(data.getCompound("inventory"));
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {

        var data = new CompoundTag();
        data.putInt("energyStored", this.energyStored);
        data.put("inventory", itemHandler.serializeNBT());
        data.putInt("litTime", this.litTime);
        pTag.put(Alkomod.MOD_ID, data);
        super.saveAdditional(pTag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int accepted = Math.min(capacity - energyStored, Math.min(amount, maxReceive));
        if (!simulate) {
            energyStored += accepted;
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        }
        return accepted;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int extracted = Math.min(energyStored, Math.min(amount, maxExtract));
        if (!simulate) {
            energyStored -= extracted;
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        }
        return extracted;
    }

    @Override
    public int getStoredEnergy() {
        return energyStored;
    }

    @Override
    public int getMaxEnergy() {
        return capacity;
    }

    @Override
    public boolean canReceive() {
        return maxReceive > 0;
    }

    @Override
    public boolean canExtract() {
        return maxExtract > 0;
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
        if(hasFuel() && !this.isLit() && getStoredEnergy() != getMaxEnergy()){
            decreaseFuelAndLit();
        }

        ticks++;
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
