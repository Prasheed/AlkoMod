package com.alko.alkomod.block.blockentity.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.block.blockentity.GeneratorBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GeneratorBlockModel extends GeoModel<GeneratorBlockEntity> {
    @Override
    public ResourceLocation getModelResource(GeneratorBlockEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/generator_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GeneratorBlockEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/block/generator_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GeneratorBlockEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/generator_block.animation.json");
    }
}
