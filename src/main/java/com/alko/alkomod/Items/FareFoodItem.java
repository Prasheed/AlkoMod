package com.alko.alkomod.Items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
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
}
