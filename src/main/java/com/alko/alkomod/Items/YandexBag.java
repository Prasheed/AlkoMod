package com.alko.alkomod.Items;

import com.alko.alkomod.Items.client.YandexBagItemRenderer;
import com.alko.alkomod.Items.client.YandexBagRenderer;
import com.alko.alkomod.capability.ModCapabilitiesRegister;
import com.alko.alkomod.energy.IBeerEnergyStorageItem;
import com.alko.alkomod.screen.CounterMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.function.Consumer;

public class YandexBag extends BeerEnergyArmorItem implements GeoItem, IBeerEnergyStorageItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public YandexBag() {
        super(ModArmorMaterials.YANDEX_ARMOR, Type.CHESTPLATE, new Properties().rarity(Rarity.RARE), 3000, 500, 500);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Сделана в память о сумки Валоди с бутылками.").withStyle(ChatFormatting.BLUE));
        tooltip.add(Component.literal("Энергия: " + getStoredEnergy(stack) + " / " + getMaxEnergy(stack)).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private YandexBagRenderer renderer;
            private YandexBagItemRenderer renderar;


            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack,
                                                                   EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.renderer == null)
                    this.renderer = new YandexBagRenderer();

                this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.renderer;
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderar == null) {
                    renderar = new YandexBagItemRenderer();
                }

                return this.renderar;
            }
        });
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            ItemStack stack = context.getItemInHand();
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            if (player != null && player.isCrouching()) {
                NetworkHooks.openScreen(player, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("YandexBag");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new CounterMenu(id, inventory, stack);
                    }
                }, buffer -> buffer.writeItem(stack));
            }
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide());
    }

    @Override
    public CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = super.getShareTag(stack);
        if (tag == null) tag = new CompoundTag();

        CompoundTag finalTag = tag;
        stack.getCapability(ModCapabilitiesRegister.INVENTORY_DATA).ifPresent(data -> {
            finalTag.put("inventoryData", data.serializeNBT());
        });

        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        super.readShareTag(stack, nbt);
        if (nbt != null && nbt.contains("inventoryData")) {
            stack.getCapability(ModCapabilitiesRegister.INVENTORY_DATA).ifPresent(data -> {
                data.deserializeNBT(nbt.getCompound("inventoryData"));
            });
        }
    }


}
