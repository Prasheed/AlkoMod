package com.alko.alkomod.Items.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.YandexBag;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class YandexBagModel extends GeoModel<YandexBag> {
    @Override
    public ResourceLocation getModelResource(YandexBag animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/yandex_bag.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(YandexBag animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/armor/yandex_bag.png");
    }

    @Override
    public ResourceLocation getAnimationResource(YandexBag animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/yandex_bag.animation.json");
    }
}
