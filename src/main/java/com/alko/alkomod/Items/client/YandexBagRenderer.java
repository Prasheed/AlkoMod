package com.alko.alkomod.Items.client;

import com.alko.alkomod.Items.YandexBag;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class YandexBagRenderer extends GeoArmorRenderer<YandexBag> {
    public YandexBagRenderer() {
        super(new YandexBagModel());
    }
}
