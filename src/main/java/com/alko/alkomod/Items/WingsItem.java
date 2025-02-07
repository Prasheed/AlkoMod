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
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.function.Consumer;

public class WingsItem extends ArmorItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

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
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private final float FLY_DURATION = 5f;

    @SuppressWarnings("deprecation")
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
