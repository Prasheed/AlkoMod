package com.alko.alkomod.handlers;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerInputHandler {

    private static final Map<Player, Boolean> HOLDING_UP = new HashMap<>();
    private static final Map<Player, Boolean> HOLDING_DOWN = new HashMap<>();
    private static final Map<Player, Boolean> HOLDING_LEFT = new HashMap<>();
    private static final Map<Player, Boolean> HOLDING_RIGHT = new HashMap<>();

    public static boolean isHoldingUp(Player player) {
        return HOLDING_UP.containsKey(player) && HOLDING_UP.get(player);
    }

    public static boolean isHoldingDown(Player player) {
        return HOLDING_DOWN.containsKey(player) && HOLDING_DOWN.get(player);
    }

    public static boolean isHoldingLeft(Player player) {
        return HOLDING_LEFT.containsKey(player) && HOLDING_LEFT.get(player);
    }

    public static boolean isHoldingRight(Player player) {
        return HOLDING_RIGHT.containsKey(player) && HOLDING_RIGHT.get(player);
    }

    public static void update(Player player, boolean up, boolean down, boolean left, boolean right) {

        HOLDING_UP.put(player, up);
        HOLDING_DOWN.put(player, down);
        HOLDING_LEFT.put(player, left);
        HOLDING_RIGHT.put(player, right);
    }

    public static void clear() {
        HOLDING_UP.clear();
        HOLDING_DOWN.clear();
        HOLDING_LEFT.clear();
        HOLDING_RIGHT.clear();
    }

    public static void remove(Player player) {
        HOLDING_UP.remove(player);
        HOLDING_DOWN.remove(player);
        HOLDING_LEFT.remove(player);
        HOLDING_RIGHT.remove(player);
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        remove(event.getEntity());
    }

    @SubscribeEvent
    public void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        remove(event.getEntity());
    }


}
