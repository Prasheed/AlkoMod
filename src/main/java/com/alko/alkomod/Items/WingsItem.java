package com.alko.alkomod.Items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class WingsItem extends ArmorItem {

    public WingsItem(Properties properties) {
        super(ArmorMaterials.DIAMOND, Type.CHESTPLATE, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this) {
            // Пример эффекта: даем игроку возможность летать
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();

            // Пример эффекта: добавляем эффект медленного падения
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200, 0));
        } else {
            // Если крылья сняты, отключаем возможность летать
            if (!player.isCreative() && !player.isSpectator()) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }
}