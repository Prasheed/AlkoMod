package com.alko.alkomod.Items;

import com.alko.alkomod.Items.client.WingsArmorRenderer;
import com.alko.alkomod.mixin.LivingEntityAccessorMixin;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class WingsItem extends ArmorItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final float FLY_DURATION = 5f;

    public WingsItem(Properties properties) {
        super(ModArmorMaterials.ANGEL_WINGS, Type.CHESTPLATE, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private WingsArmorRenderer renderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new WingsArmorRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        LivingEntity entity = (LivingEntity) animationState.getData(DataTickets.ENTITY);

        if (entity != null) {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);

            if (itemStack.hasTag()) {
                CompoundTag nbt = itemStack.getTag();

                int animation_id = nbt.getInt("animation_id");

                switch (animation_id){
                    case 1:
                        animationState.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
                        break;
                    case 2:
                        animationState.getController().setAnimation(RawAnimation.begin().then("gliding", Animation.LoopType.LOOP));
                        break;
                    default:
                        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                        break;
                }

            } else {
                animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            }
        }

        return PlayState.CONTINUE;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        System.out.println("ArmorTick");
        boolean isWearing = player.getInventory().getArmor(EquipmentSlot.CHEST.getIndex()).getItem() == this;
        boolean isJumping = ((LivingEntityAccessorMixin) player).is_jumping();

        CompoundTag tag = stack.getOrCreateTag();

        if (tag.contains("duration") && tag.contains("animation_id")){
            if (isWearing && isJumping && !player.onGround()){
                if (tag.getFloat("duration") >= 0.0F) {
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, 0.3, currentMotion.z);
                    tag.putInt("animation_id", 1);
                    tag.putBoolean("isFlying", true);
                    if(player.tickCount % 20 == 0){
                        tag.putFloat("duration", tag.getFloat("duration")-1f);
                    }
                } else {
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, -0.05, currentMotion.z);
                    tag.putInt("animation_id", 2);
                }
            }
        }else{
            tag.putFloat("duration", FLY_DURATION);
            tag.putInt("animation_id", 0);
        }

        if (player.onGround()){
            tag.putFloat("duration", FLY_DURATION);
            tag.putInt("animation_id", 0);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
