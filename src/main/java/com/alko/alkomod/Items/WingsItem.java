package com.alko.alkomod.Items;

import com.alko.alkomod.Items.client.WingsArmorRenderer;
import com.alko.alkomod.handlers.PlayerAnimationStateHandler;
import com.alko.alkomod.handlers.PlayerInputHandler;
import com.alko.alkomod.mixin.LivingEntityAccessorMixin;
import com.alko.alkomod.network.CAnimationStateUpdate;
import com.alko.alkomod.network.PacketHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
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

import java.util.UUID;
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
        Player player = (Player) animationState.getData(DataTickets.ENTITY);

        UUID uuid = player.getUUID();
        String state = PlayerAnimationStateHandler.getPlayerAnimationStateFromKey(uuid,"angel_wings");
        switch (state){
            case "flying":
                animationState.getController().setAnimation(RawAnimation.begin().then("fly", Animation.LoopType.LOOP));
                break;
            case "gliding":
                animationState.getController().setAnimation(RawAnimation.begin().then("gliding", Animation.LoopType.LOOP));
                break;
            default:
                animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                break;
        }

        return PlayState.CONTINUE;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        CompoundTag tag = stack.getTag();
        if (tag.contains("duration")){
            if (PlayerInputHandler.isHoldingSpace(player)){

                if (tag.getInt("duration") > 0){
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, 0.3, currentMotion.z);
                    // ТУТ
                    System.out.println(tag.getInt("duration"));
                    if(level.isClientSide())PlayerAnimationStateHandler.changeValueFromPlayerMap(player.getUUID(),player,"angel_wings", "flying");
                    if (player.tickCount % 20 == 0){
                        tag.putInt("duration", tag.getInt("duration") - 1);
                    }
                }else{
                    if(level.isClientSide()) PlayerAnimationStateHandler.changeValueFromPlayerMap(player.getUUID(),player,"angel_wings", "gliding");
                    Vec3 currentMotion = player.getDeltaMovement();
                    player.setDeltaMovement(currentMotion.x, -0.2, currentMotion.z);
                    // ТУТ
                }

            }
            // ТУТ
            if (player.onGround() && level.isClientSide())
                PlayerAnimationStateHandler.changeValueFromPlayerMap(player.getUUID(), player, "angel_wings", "idle");
            if (tag.getInt("duration") != 0 || tag.getInt("duration") <= 0)
                if (player.onGround()) {
                    tag.putInt("duration", (int) FLY_DURATION);
                }
        }else{
            tag.putInt("duration", (int) FLY_DURATION);
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
