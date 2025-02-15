package com.alko.alkomod.Items.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.HuTao;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HuTaoModel extends GeoModel<HuTao> {
    @Override
    public ResourceLocation getModelResource(HuTao animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/hu_tao.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HuTao animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/item/hu_tao.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HuTao animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/hu_tao.animation.json");
    }
}
