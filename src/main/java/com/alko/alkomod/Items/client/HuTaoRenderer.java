package com.alko.alkomod.Items.client;

import com.alko.alkomod.Items.HuTao;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HuTaoRenderer extends GeoItemRenderer<HuTao> {
    public HuTaoRenderer() {
        super(new HuTaoModel());
    }
}
