package com.alko.alkomod.handlers;

import com.alko.alkomod.network.CAnimationStateUpdate;
import com.alko.alkomod.network.PacketHandler;
import com.alko.alkomod.network.SAnimationStateUpdate;
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

    public static void changeValueFromPlayerMap(Player player, String key, String value, boolean toServer){
        UUID uuid = player.getUUID();
        HashMap<String, String> map = allPlayerAnimationStates.get(uuid);
        if(allPlayerAnimationStates.getOrDefault(player.getUUID(),new HashMap<>()).get(key).equals(value)) return;
        if(player!=null){
            if(!toServer){
                System.out.println("Отправлено на сервер");
                PacketHandler.sendToServer(new SAnimationStateUpdate(player.getUUID(),key,value));
                allPlayerAnimationStates.getOrDefault(uuid, new HashMap<>()).put(key, value);
            }else{
                System.out.println("Отправлено на клиент");
                PacketHandler.sendToTrackingAndSelf(new CAnimationStateUpdate(player.getUUID(),key,value),player);
                allPlayerAnimationStates.getOrDefault(uuid, new HashMap<>()).put(key, value);
            }
        }
        //allPlayerAnimationStates.getOrDefault(uuid, new HashMap<String, String>()).put(key, value);
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
