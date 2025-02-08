package com.alko.alkomod.handlers;

import com.alko.alkomod.network.CAnimationStateUpdate;
import com.alko.alkomod.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Minecart;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerAnimationStateHandler {

    public static final HashMap<UUID, HashMap<String, String>> allPlayerAnimationStates = new HashMap<>();
    public static final UUID defaultUUID = UUID.randomUUID();

    public static void addPlayerAndMap(UUID uuid, HashMap<String, String> map){
        allPlayerAnimationStates.put(uuid, map);
    }

    public static HashMap<String, String> getPlayerMap(UUID uuid){
        return allPlayerAnimationStates.get(uuid);
    }

    public static void changeValueFromPlayerMap(UUID uuid, @Nullable Player player, String key, String value){
        HashMap<String, String> map = allPlayerAnimationStates.get(uuid);
        if(map == null) map = new HashMap<String, String>();

        if(!map.getOrDefault(key, "idle").equals(value)){
            if(player!=null && !player.level().isClientSide()) {
                PacketHandler.sendToTrackingAndSelf(new CAnimationStateUpdate(player.getUUID(), key, value), player);
            }
            allPlayerAnimationStates.getOrDefault(uuid, new HashMap<String, String>()).put(key, value);
        }
    }

    public static String getPlayerAnimationStateFromKey(UUID uuid, String key){
        return allPlayerAnimationStates.get(uuid) == null ? "idle" : allPlayerAnimationStates.get(uuid).getOrDefault(key,"idle");
    }

    public static void init(UUID uuid){
        HashMap<String, String> map = new HashMap<>();
        map.put("angel_wings", "idle");


        allPlayerAnimationStates.put(uuid, map);
    }

}
