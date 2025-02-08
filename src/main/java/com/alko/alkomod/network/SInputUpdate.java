package com.alko.alkomod.network;

import com.alko.alkomod.handlers.PlayerInputHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SInputUpdate {
    private boolean space;
    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;

    public SInputUpdate(boolean space,boolean up, boolean down, boolean left, boolean right){
        this.space = space;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public SInputUpdate(FriendlyByteBuf buf){
        this(buf.readBoolean(),buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBoolean(this.space);
        buf.writeBoolean(this.up);
        buf.writeBoolean(this.down);
        buf.writeBoolean(this.left);
        buf.writeBoolean(this.right);
    }


    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(()->{
            Player player = context.get().getSender();
            if(player != null){
                PlayerInputHandler.update(player, this.space, this.up, this.down, this.left, this.right);
            }
        });
        context.get().setPacketHandled(true);
    }
}
