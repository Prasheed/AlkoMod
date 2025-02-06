package com.alko.alkomod.events;


import com.alko.alkomod.Alkomod;
import com.alko.alkomod.handlers.PlayerInputHandler;
import com.alko.alkomod.network.PacketHandler;
import com.alko.alkomod.network.SInputUpdate;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.system.Platform;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEvents {
    private static boolean lastUpState = false;
    private static boolean lastDownState = false;
    private static boolean lastLeftState = false;
    private static boolean lastRightState = false;

//    @SubscribeEvent
//    public static void inputEvent(InputEvent.Key event){
//        Player player = Minecraft.getInstance().player;
//        if (player == null || event.getAction() != InputConstants.PRESS || Minecraft.getInstance().screen != null) {
//            return;
//        }
//        Input input = Minecraft.getInstance().player.input;
//        System.out.println("up "+input.up);
//        System.out.println("down "+input.down);
//        System.out.println("left "+input.left);
//        System.out.println("right "+input.right);
//        //PacketHandler.sendToServer(new SInputUpdate(input.up, input.down, input.left, input.right));
//    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent evt) {
        if (evt.phase == TickEvent.Phase.END) {
            System.out.println("aaawdawdawd");
            updateInputAndSend();
        }
    }

    private static void updateInputAndSend() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            boolean upState = mc.player.input.up;
            boolean downState = mc.player.input.down;
            boolean leftState = mc.player.input.left;
            boolean rightState = mc.player.input.right;
            if (upState != lastUpState || downState != lastDownState || leftState != lastLeftState || rightState != lastRightState) {
                lastUpState = upState;
                lastDownState = downState;
                lastLeftState = leftState;
                lastRightState = rightState;
                PacketHandler.sendToServer(new SInputUpdate(upState,downState,leftState,rightState));
                PlayerInputHandler.update(mc.player, upState, downState, leftState, rightState);
            }
        }
    }
}
