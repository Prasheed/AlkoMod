package com.alko.alkomod.Items.client;

import com.alko.alkomod.Items.custom.GeneratorBlockItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GeneratorBlockItemRenderer extends GeoItemRenderer<GeneratorBlockItem> {
    public GeneratorBlockItemRenderer() {
        super(new GeneratorBlockItemModel());
    }
}

