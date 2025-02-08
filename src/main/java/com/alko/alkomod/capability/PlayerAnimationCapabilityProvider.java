package com.alko.alkomod.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerAnimationCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<PlayerAnimationCapability> PLAYER_ANIMATION_STATE = CapabilityManager.get(new CapabilityToken<PlayerAnimationCapability>() {});

    public PlayerAnimationCapability playerAnimationCapability = null;
    private final LazyOptional<PlayerAnimationCapability> optional = LazyOptional.of(this::creatPlayerAnimationCapability);

    private PlayerAnimationCapability creatPlayerAnimationCapability() {
        if (this.playerAnimationCapability == null){
            this.playerAnimationCapability = new PlayerAnimationCapability();

        }
        return this.playerAnimationCapability;
    }


    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
        if (capability == PLAYER_ANIMATION_STATE){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        creatPlayerAnimationCapability().saveNBTdata(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        creatPlayerAnimationCapability().loadNBTdata(nbt);
    }
}
