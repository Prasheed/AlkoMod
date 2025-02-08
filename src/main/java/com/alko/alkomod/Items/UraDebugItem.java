package com.alko.alkomod.Items;

import com.alko.alkomod.capability.PlayerAnimationCapabilityProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class UraDebugItem extends Item {
    public UraDebugItem() {
        super(new Item.Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level Level, Player player, InteractionHand pUsedHand) {
        if(!Level.isClientSide()){
            player.getCapability(PlayerAnimationCapabilityProvider.PLAYER_ANIMATION_STATE).ifPresent(cap -> {
                cap.getAnimationStateMap().put("angel_wings", "flying");
                System.out.println("Записано " + cap.getAnimationState("angel_wings"));
            });
        }
        return super.use(Level, player, pUsedHand);
    }
}
