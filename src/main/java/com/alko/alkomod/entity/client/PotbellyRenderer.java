package com.alko.alkomod.entity.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.entity.custom.PotbellyEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PotbellyRenderer extends GeoEntityRenderer<PotbellyEntity> {
    public PotbellyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PotbellyModel());
    }

    @Override
    public ResourceLocation getTextureLocation(PotbellyEntity animatable) {
        return new ResourceLocation(Alkomod.MOD_ID, "textures/entity/potbelly.png");
    }

    @Override
    public void render(PotbellyEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.4f, 0.4f, 0.4f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}