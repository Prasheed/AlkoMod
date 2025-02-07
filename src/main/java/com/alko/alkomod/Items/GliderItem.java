package com.alko.alkomod.Items;

import com.alko.alkomod.handlers.PlayerInputHandler;
import com.alko.alkomod.mixin.LivingEntityAccessorMixin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class GliderItem extends ArmorItem {

    private final float FLY_DURATION = 5f;
    private final float GLIDE_SPEED = 0.1f;
    private static final UUID GLIDE_SPEED_MODIFIER_ID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

    public GliderItem(Item.Properties properties) {
        super(ArmorMaterials.DIAMOND, ArmorItem.Type.CHESTPLATE, properties);
    }


    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        boolean isWearing = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this;
        boolean isJumping = ((LivingEntityAccessorMixin) player).is_jumping();
        //Input input = Minecraft.getInstance().player.input;
        //System.out.println(player.getSpeed());

        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("duration") && tag.contains("isGlideSpeedMod")){
            if (tag.getFloat("duration") > 0f) {
                if (isWearing && isJumping && !player.onGround()) {
                    if (player.tickCount % 20 == 0) tag.putFloat("duration", tag.getFloat("duration") - 1f);
                    if (player.isCrouching()) {
                        if(!tag.getBoolean("isGlideSpeedMod")){
                            player.setSpeed((float) (player.getSpeed() + 1.4));
                            tag.putBoolean("isGlideSpeedMod", true);
                        }

                        Vec3 currentMotion = player.getDeltaMovement();
                        player.setDeltaMovement(currentMotion.x,0,currentMotion.z);

                        if (PlayerInputHandler.isHoldingUp(player)) {
                            player.moveRelative(1, new Vec3(0, 0, GLIDE_SPEED));
                        }
                        if (PlayerInputHandler.isHoldingDown(player)) {
                            player.moveRelative(1, new Vec3(0, 0, -GLIDE_SPEED * 0.8F));
                        }
                        if (PlayerInputHandler.isHoldingLeft(player)) {
                            player.moveRelative(1, new Vec3(GLIDE_SPEED, 0, 0));
                        }
                        if (PlayerInputHandler.isHoldingRight(player)) {
                            player.moveRelative(1, new Vec3(-GLIDE_SPEED, 0, 0));
                        }
                        if (!player.getCommandSenderWorld().isClientSide()) {
                            player.fallDistance = 0.0F;
//                            if (player instanceof ServerPlayer) {
//                                ((ServerPlayer) player).connection.aboveGroundTickCount = 0;
//                            }
                        }

                    } else {
                        if(tag.getBoolean("isGlideSpeedMod")){
                            player.setSpeed((float) (player.getSpeed() - 1.4));

                            tag.putBoolean("isGlideSpeedMod", false);
                        }
                        Vec3 currentMotion = player.getDeltaMovement();
                        player.setDeltaMovement(currentMotion.x, 0.3, currentMotion.z);

                    }
                }
            } else {

                Vec3 currentMotion = player.getDeltaMovement();
                player.setDeltaMovement(currentMotion.x, -0.2, currentMotion.z);

            }


        } else {
            tag.putFloat("duration", FLY_DURATION);
            tag.putBoolean("isGlideSpeedMod", false);
        }

        if (player.onGround()){
            tag.putFloat("duration", FLY_DURATION);
        }
    }
}