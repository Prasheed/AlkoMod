package com.alko.alkomod.handlers;

import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationStateHandler {

    public static final Map<Player, HashMap<String, String>> allPlayerAnimationStates = new HashMap<>();

    public void addPlayerAndMap(Player player, HashMap<String, String> map){
        allPlayerAnimationStates.put(player, map);
    }

    public HashMap<String, String> getPlayerMap(Player player){
        return allPlayerAnimationStates.get(player);
    }

    public void changeValueFromPlayerMap(Player player, String key, String value){
        allPlayerAnimationStates.get(player).put(key, value);
    }

}
