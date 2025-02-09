package com.alko.alkomod.network;

import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class CAnimationStateUpdate {

    private UUID uuid;
    private String key;
    private String state;

    public CAnimationStateUpdate(UUID uuid, String key, String value){
        this.uuid = uuid;
        this.key = key;
        this.state = value;
    }

    public CAnimationStateUpdate(FriendlyByteBuf buf){
        this(buf.readUUID(), buf.readUtf(), buf.readUtf());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeUtf(key);
        buf.writeUtf(state);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            System.out.println("Пакет обновления анимации на клиенте "+this.uuid+ " " + this.key + " " + this.state);
            PlayerAnimationStateHandler.applyOtherPlayerAnimationState(this.uuid, this.key, this.state);
        });
        context.get().setPacketHandled(true);
    }
}
