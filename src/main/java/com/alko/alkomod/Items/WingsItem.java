package com.alko.alkomod.Items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WingsItem extends ArmorItem {

    private static final double VERTICAL_SPEED = 0.5; // Скорость подъема и спуска

    public WingsItem(Properties properties) {
        super(ArmorMaterials.DIAMOND, Type.CHESTPLATE, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            boolean isWearing = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this;

            if (isWearing) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            } else {
                if (!player.isCreative() && !player.isSpectator()) {
                    player.getAbilities().mayfly = false;
                    player.getAbilities().flying = false;
                    player.onUpdateAbilities();
                }
            }
        }

        // Логика управления полетом должна быть только на клиенте!
        if (level.isClientSide && player.getAbilities().flying) {
            Vec3 motion = player.getDeltaMovement();


        }
    }
}
