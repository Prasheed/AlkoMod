package com.alko.alkomod.Items;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FareFoodItem extends Item {
    public FareFoodItem(Properties properties) {
        super(properties.food(new FoodProperties.Builder()
                .nutrition(4) // Количество восстанавливаемых единиц голода
                .saturationMod(0.3F) // Коэффициент насыщения
                .effect(() -> new MobEffectInstance(MobEffects.JUMP, 200, 1), 1.0F) // Эффект прыгучести на 10 секунд (200 тиков)
                .alwaysEat() // Можно есть даже при полном голоде
                .build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.setDayTime(13000); // Устанавливаем время на ночь (13000 — начало ночи)
            }
        }
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (!player.level().isClientSide) {
            if (target instanceof ServerPlayer targetPlayer) {
                targetPlayer.connection.disconnect(Component.literal("Это глобальный отсос")); // Кикаем игрока
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
