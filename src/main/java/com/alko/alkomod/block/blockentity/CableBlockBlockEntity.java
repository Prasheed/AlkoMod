package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CableBlockBlockEntity extends BlockEntity {

    private int networkId = -1;

    public CableBlockBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntity.CABLE_BLOCK_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
        setChanged();
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        CompoundTag tag = pTag.getCompound(Alkomod.MOD_ID);
        this.networkId = tag.getInt("networkId");
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        CompoundTag tag = pTag.getCompound(Alkomod.MOD_ID);
        tag.putInt("networkId", this.networkId);
        pTag.put(Alkomod.MOD_ID, tag);
        super.saveAdditional(pTag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
