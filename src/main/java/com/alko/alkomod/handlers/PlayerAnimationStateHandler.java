package com.alko.alkomod.handlers;

import com.alko.alkomod.network.CAnimationStateUpdate;
import com.alko.alkomod.network.PacketHandler;
import com.alko.alkomod.network.SAnimationStateUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerAnimationStateHandler {

    public static final HashMap<UUID, HashMap<String, String>> allPlayerAnimationStates = new HashMap<>();
    public static final UUID defaultUUID = UUID.randomUUID();

    @OnlyIn(Dist.CLIENT)
    public static void changeSelfStateAndNotifyServer(Player player, String key, String state){
        if (allPlayerAnimationStates.get(player.getUUID()).get(key).equals(state)) return;
        allPlayerAnimationStates.get(player.getUUID()).put(key, state);
        PacketHandler.sendToServer(new SAnimationStateUpdate(player.getUUID(), key, state));
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void updatePlayerAnimationStateAndNotifyNear(Player player, String key, String state){
        allPlayerAnimationStates.get(player.getUUID()).put(key, state);
        PacketHandler.sendToTracking(new CAnimationStateUpdate(player.getUUID(), key, state), player);
        System.out.println("Обновлено на сервере");
    }

    public static void init(UUID uuid){
        HashMap<String, String> map = new HashMap<>();
        map.put("angel_wings", "idle");


        allPlayerAnimationStates.put(uuid, map);
    }

    @OnlyIn(Dist.CLIENT)
    public static void applyOtherPlayerAnimationState(UUID uuid, String key, String state) {
        allPlayerAnimationStates.get(uuid).put(key, state);
        System.out.println("Обновлено на клиенте");
    }

    @OnlyIn(Dist.CLIENT)
    public static String getPlayerAnimationState(UUID uuid, String key) {
        return allPlayerAnimationStates.getOrDefault(uuid,new HashMap<>()).getOrDefault(key,"idle");
    }
}
