package com.alko.alkomod.network;

import com.alko.alkomod.Alkomod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(Alkomod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.messageBuilder(SInputUpdate.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(SInputUpdate::encode)
                .decoder(SInputUpdate::new)
                .consumerMainThread(SInputUpdate::handle)
                .add();

        INSTANCE.messageBuilder(CAnimationStateUpdate.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(CAnimationStateUpdate::encode)
                .decoder(CAnimationStateUpdate::new)
                .consumerMainThread(CAnimationStateUpdate::handle)
                .add();
    }

    public static void sendToServer(Object msg){
        INSTANCE.send(PacketDistributor.SERVER.noArg(),msg);
    }

    public static void sendToTrackingAndSelf(Object msg, Entity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }
}
