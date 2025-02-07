package com.alko.alkomod.Items.client;

import com.alko.alkomod.Items.WingsItem;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WingsArmorRenderer extends GeoArmorRenderer<WingsItem> {
    public WingsArmorRenderer() {
        super(new WingsArmorModel());
    }
}
