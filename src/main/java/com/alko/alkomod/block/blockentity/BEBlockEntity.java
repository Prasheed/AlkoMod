package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public  abstract class BEBlockEntity extends BlockEntity implements IBeerEnergyStorageBlock {
    int capacity;
    int maxExtract;
    int maxReceive;
    int storedEnergy;

    public BEBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.capacity = 10000;
        this.maxExtract = 10;
        this.maxReceive = 10;
        this.storedEnergy = 0;
    }

    @Override
    public int receiveEnergy(int amount, boolean simulate) {
        int accepted = Math.min(capacity - storedEnergy, Math.min(amount, maxReceive));
        if (!simulate) {
            storedEnergy += accepted;
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        }
        return accepted;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate) {
        int extracted = Math.min(storedEnergy, Math.min(amount, maxExtract));
        if (!simulate) {
            storedEnergy -= extracted;
            setChanged();
            this.level.sendBlockUpdated(this.worldPosition,getBlockState(),getBlockState(), Block.UPDATE_ALL);
        }
        return extracted;
    }

    @Override
    public int getStoredEnergy() {
        return this.storedEnergy;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public int getMaxExtract() {
        return this.maxExtract;
    }

    @Override
    public int getMaxReceive() {
        return this.maxReceive;
    }

    @Override
    public boolean isFull() {
        return !(this.storedEnergy < this.capacity);
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
        this.storedEnergy = data.getInt("storedEnergy");
        this.capacity = data.getInt("capacity");
        this.maxReceive = data.getInt("maxReceive");
        this.maxExtract = data.getInt("maxExtract");
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        var data = pTag.getCompound(Alkomod.MOD_ID);
        data.putInt("storedEnergy", this.storedEnergy);
        data.putInt("capacity", this.capacity);
        data.putInt("maxReceive", this.maxReceive);
        data.putInt("maxExtract", this.maxExtract);
        pTag.put(Alkomod.MOD_ID, data);
        super.saveAdditional(pTag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
