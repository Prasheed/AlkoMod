package com.alko.alkomod.Items.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.custom.GeneratorBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeneratorBlockItemModel extends GeoModel<GeneratorBlockItem> {
    public ResourceLocation getModelResource(GeneratorBlockItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/generator_block.geo.json");
    }

    public ResourceLocation getTextureResource(GeneratorBlockItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/block/generator_block.png");
    }

    public ResourceLocation getAnimationResource(GeneratorBlockItem animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/generator_block.animation.json");
    }
}

