package com.alko.alkomod.Items;

import com.alko.alkomod.mixin.LivingEntityAccessorMixin;
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
        boolean isWearing = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this;
        boolean isJumping = ((LivingEntityAccessorMixin) player).is_jumping();
        if (isJumping){
            System.out.println("прыгаю");
        }
    }
}
