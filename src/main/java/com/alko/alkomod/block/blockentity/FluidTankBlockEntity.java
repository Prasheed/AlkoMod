package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.screen.FluidTankBlockMenu;
import com.alko.alkomod.util.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidTankBlockEntity extends BlockEntity implements MenuProvider, TickableBlockEntity {
    private static final Component TITLE =
            Component.literal("pizda");

    private final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            FluidTankBlockEntity.this.sendUpdate();
        }
    };

    private final LazyOptional<ItemStackHandler> inventoryOptional = LazyOptional.of(() -> this.inventory);

    private final FluidTank fluidTank = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            FluidTankBlockEntity.this.sendUpdate();
        }
    };

    private final LazyOptional<FluidTank> fluidOptional = LazyOptional.of(() -> this.fluidTank);

    public FluidTankBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.FLUID_TANK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void tick() {
        if (this.level == null || this.level.isClientSide())
            return;

        ItemStack stack = this.inventory.getStackInSlot(0);
        if(stack.isEmpty())
            return;

        if(this.fluidTank.getFluidAmount() >= this.fluidTank.getCapacity())
            return;

        LazyOptional<IFluidHandlerItem> fluidHandler = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        fluidHandler.ifPresent(iFluidHandlerItem -> {
            if (!fluidTank.getFluid().isEmpty())
                if(!this.fluidTank.getFluid().isFluidEqual(iFluidHandlerItem.getFluidInTank(0)))
                    return;

            int amountToDrain = this.fluidTank.getCapacity() - this.fluidTank.getFluidAmount();
            int amount = iFluidHandlerItem.drain(amountToDrain, IFluidHandler.FluidAction.SIMULATE).getAmount();
            System.out.println(amount +" " + amountToDrain);
            if(amount > 0) {
                this.fluidTank.fill(iFluidHandlerItem.drain(amountToDrain, IFluidHandler.FluidAction.EXECUTE), IFluidHandler.FluidAction.EXECUTE);

                if(amount <= amountToDrain) {
                    this.inventory.setStackInSlot(0, iFluidHandlerItem.getContainer());
                }
            }
        });



    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("Inventory", this.inventory.serializeNBT());
        pTag.put("FluidTank", this.fluidTank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.inventory.deserializeNBT(pTag.getCompound("Inventory"));
        this.fluidTank.readFromNBT(pTag.getCompound("FluidTank"));
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return this.inventoryOptional.cast();

        if(cap == ForgeCapabilities.FLUID_HANDLER)
            return this.fluidOptional.cast();

        return super.getCapability(cap);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryOptional.invalidate();
        this.fluidOptional.invalidate();
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        saveAdditional(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return TITLE;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new FluidTankBlockMenu(pContainerId, pPlayerInventory, this);
    }

    private void sendUpdate() {
        setChanged();

        if (this.level != null)
            this.level.sendBlockUpdated(this.worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    public LazyOptional<ItemStackHandler> getInventoryOptional() {
        return this.inventoryOptional;
    }

    public LazyOptional<FluidTank> getFluidOptional() {
        return this.fluidOptional;
    }

    public ItemStackHandler getInventory() {
        return this.inventory;
    }

    public FluidTank getFluidTank() {
        return this.fluidTank;
    }
}
