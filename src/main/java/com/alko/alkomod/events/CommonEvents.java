package com.alko.alkomod.events;


import com.alko.alkomod.Alkomod;
import com.alko.alkomod.capability.PlayerAnimationCapability;
import com.alko.alkomod.capability.PlayerAnimationCapabilityProvider;
import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import com.alko.alkomod.network.PacketHandler;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID)
public class CommonEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        UUID playerUUID = player.getUUID();
        PlayerAnimationStateHandler.init(player.getUUID());
    }
}
