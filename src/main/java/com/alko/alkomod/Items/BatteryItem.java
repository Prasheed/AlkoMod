package com.alko.alkomod.Items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BatteryItem extends BeerEnergyItem {
    public BatteryItem() {
        super(new Item.Properties().stacksTo(1), 10000, 500, 500);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Энергия: " + getStoredEnergy(stack) + " / " + getMaxEnergy(stack)).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        if(pUsedHand == InteractionHand.MAIN_HAND){
            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            if(stack.getItem() instanceof BatteryItem item){
                if(!level.isClientSide() && player.isCrouching()) {
                    player.sendSystemMessage(Component.literal(String.valueOf(getStoredEnergy(stack))));
                }
                item.receiveEnergy(stack, 50,false);
            }
        }
        return super.use(level, player, pUsedHand);
    }
}
