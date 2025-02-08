package com.alko.alkomod.network;

import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import com.alko.alkomod.handlers.PlayerInputHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CAnimationStateUpdate {

    private UUID uuid;
    private String key;
    private String value;

    public CAnimationStateUpdate(UUID uuid, String key, String value){
        this.uuid = uuid;
        this.key = key;
        this.value = value;
    }

    public CAnimationStateUpdate(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readUtf(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeUtf(key);
        buf.writeUtf(value);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            System.out.println("Пакет обновления анимации "+ this.key + " " + this.value);
            PlayerAnimationStateHandler.changeValueFromPlayerMap(this.uuid,null, this.key,this.value);
        });
        context.get().setPacketHandled(true);
    }
}
