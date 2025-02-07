package com.alko.alkomod.Items.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.WingsItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WingsArmorModel extends GeoModel<WingsItem> {
    @Override
    public ResourceLocation getModelResource(WingsItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/wings.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WingsItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/armor/wings.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WingsItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/wings.animation.json");
    }
}
