package com.alko.alkomod.block.blockentity.client;

import com.alko.alkomod.block.GeneratorBlock;
import com.alko.alkomod.block.blockentity.GeneratorBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GeneratorBlockRenderer extends GeoBlockRenderer<GeneratorBlockEntity> {
    public GeneratorBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(new GeneratorBlockModel());
    }
}