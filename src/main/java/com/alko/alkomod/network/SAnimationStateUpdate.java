package com.alko.alkomod.network;

import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class SAnimationStateUpdate {

    private UUID uuid;
    private String key;
    private String value;

    public SAnimationStateUpdate(UUID uuid, String key, String value){
        this.uuid = uuid;
        this.key = key;
        this.value = value;
    }

    public SAnimationStateUpdate(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readUtf(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeUtf(key);
        buf.writeUtf(value);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            Player player = context.get().getSender();
            if(player!=null) {
                PlayerAnimationStateHandler.updatePlayerAnimationStateAndNotifyNear(player, this.key, this.value);
            }
        });
        context.get().setPacketHandled(true);
    }
}
