package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.energy.IBeerEnergyStorageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public  abstract class BEBlockEntity extends BlockEntity implements IBeerEnergyStorageBlock {
    public static final ModelProperty<Map<Direction, EnergySide>> ENERGY_SIDES = new ModelProperty<>();
    Map<Direction, EnergySide> sideConfig = new HashMap<>();
    int capacity;
    int maxExtract;
    int maxReceive;
    int storedEnergy;

    public BEBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        for (Direction dir : Direction.values()) {
            sideConfig.put(dir, EnergySide.NONE);
        }
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

        Map<Direction, EnergySide> loadedSideMode = new HashMap<>();
        CompoundTag sideModeTag = data.getCompound("SideModes");
        // Проходим по всем тегам внутри sideMode и восстанавливаем карту
        for (String key : sideModeTag.getAllKeys()) {
            Direction direction = Direction.byName(key); // Получаем Direction из строки
            if (direction != null) {
                int energySideOrdinal = sideModeTag.getInt(key); // Получаем индекс enum
                EnergySide energySide = EnergySide.values()[energySideOrdinal]; // Восстанавливаем enum из индекса
                loadedSideMode.put(direction, energySide);
            }
        }
        this.sideConfig = loadedSideMode;
    }

    @Override
    public void saveAdditional(CompoundTag pTag) {
        CompoundTag data = pTag.getCompound(Alkomod.MOD_ID);
        CompoundTag sideModeTag = new CompoundTag();

        for (Map.Entry<Direction, EnergySide> entry : sideConfig.entrySet()) {
            Direction direction = entry.getKey();
            EnergySide energySide = entry.getValue();

            // Преобразуем Direction в строку, например, используем name()
            sideModeTag.putInt(direction.getName(), energySide.ordinal()); // Сохраняем как int (например, enum EnergySide)
        }

        data.putInt("storedEnergy", this.storedEnergy);
        data.putInt("capacity", this.capacity);
        data.putInt("maxReceive", this.maxReceive);
        data.putInt("maxExtract", this.maxExtract);
        data.put("SideModes", sideModeTag);
        pTag.put(Alkomod.MOD_ID, data);
        super.saveAdditional(pTag);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void setSideMode(Direction dir, EnergySide mode) {
        sideConfig.put(dir, mode);
        if (level != null) {
            setChanged();  // Уведомляем Minecraft, что данные изменились
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public EnergySide getSideMode(Direction dir) {
        return sideConfig.getOrDefault(dir, EnergySide.NONE);
    }
}

