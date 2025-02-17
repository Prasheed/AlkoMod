package com.alko.alkomod.Items.client;

import com.alko.alkomod.Items.YandexBag;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class YandexBagItemRenderer extends GeoItemRenderer<YandexBag> {
    public YandexBagItemRenderer() {
        super(new YandexBagModel());
    }
}
