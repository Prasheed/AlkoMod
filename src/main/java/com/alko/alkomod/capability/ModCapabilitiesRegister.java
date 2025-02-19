package com.alko.alkomod.capability;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.util.ICountData;
import com.alko.alkomod.util.IItemStackHandlerData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID)
public class ModCapabilitiesRegister {

    // Создаём уникальный Capability для хранения нашего счётчика
    public static final Capability<ICountData> COUNT_DATA = CapabilityManager.get(new CapabilityToken<>() {});
    public static final Capability<IItemStackHandlerData> INVENTORY_DATA = CapabilityManager.get(new CapabilityToken<>() {});



    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(PlayerAnimationCapabilityProvider.PLAYER_ANIMATION_STATE).isPresent()) {
                event.addCapability(new ResourceLocation(Alkomod.MOD_ID, "properties"), new PlayerAnimationCapabilityProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        if(event.isWasDeath()) {
            event.getOriginal().getCapability(PlayerAnimationCapabilityProvider.PLAYER_ANIMATION_STATE).ifPresent(oldStore -> {
                event.getOriginal().getCapability(PlayerAnimationCapabilityProvider.PLAYER_ANIMATION_STATE).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PlayerAnimationCapability.class);
        event.register(IItemStackHandlerData.class);
    }
}
