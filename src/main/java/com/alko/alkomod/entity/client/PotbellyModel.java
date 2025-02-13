package com.alko.alkomod.entity.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.entity.custom.PotbellyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PotbellyModel extends GeoModel<PotbellyEntity> {
    @Override
    public ResourceLocation getModelResource(PotbellyEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "geo/potbelly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(PotbellyEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/entity/potbelly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(PotbellyEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "animations/potbelly.animation.json");
    }

    @Override
    public void setCustomAnimations(PotbellyEntity animatable, long instanceId, AnimationState<PotbellyEntity> animationState) { // бошка двигается
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}