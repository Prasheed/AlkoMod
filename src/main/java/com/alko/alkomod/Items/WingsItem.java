package com.alko.alkomod.Items;

import com.alko.alkomod.mixin.LivingEntityAccessorMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WingsItem extends ArmorItem {

    public WingsItem(Properties properties) {
        super(ArmorMaterials.DIAMOND, Type.CHESTPLATE, properties);
    }
    private final float FLY_DURATION = 5f;

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        boolean isWearing = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this;
        boolean isJumping = ((LivingEntityAccessorMixin) player).is_jumping();
        System.out.println(player.onGround());

        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("duration")){

            if (isWearing && isJumping && !player.onGround()){
                System.out.println(tag.getFloat("duration"));

                if (tag.getFloat("duration") >= 0.0F) {
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, 0.3, currentMotion.z);

                    if(player.tickCount % 20 == 0){
                        tag.putFloat("duration", tag.getFloat("duration")-1f);
                    }
                } else {
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, -0.05, currentMotion.z);
                }
            }
        }else tag.putFloat("duration", FLY_DURATION);

        if (player.onGround()){
            tag.putFloat("duration", FLY_DURATION);
        }
    }
}
