package com.alko.alkomod.block.blockentity.client;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.WrenchItem;
import com.alko.alkomod.block.blockentity.BEBlockEntity;
import com.alko.alkomod.block.blockentity.EnergySide;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Matrix4f;

public class UniversalBEBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {

    private static final ResourceLocation INPUT_OVERLAY = new ResourceLocation(Alkomod.MOD_ID, "textures/block/energy_input_mode.png");
    private static final ResourceLocation OUTPUT_OVERLAY = new ResourceLocation(Alkomod.MOD_ID, "textures/block/energy_output_mode.png");

    public UniversalBEBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Minecraft minecraft = Minecraft.getInstance();
        if(minecraft.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof WrenchItem){
            int block = blockEntity.getLevel().getBrightness(LightLayer.BLOCK, blockEntity.getBlockPos().above());
            int sky = blockEntity.getLevel().getBrightness(LightLayer.SKY, blockEntity.getBlockPos().above());
            int brightness = LightTexture.pack(block,sky);
            for (Direction direction : Direction.values()) {
                if (isInput(blockEntity, direction)) {
                    renderOverlay(poseStack, bufferSource, INPUT_OVERLAY, direction, brightness);
                } else if (isOutput(blockEntity, direction)) {
                    renderOverlay(poseStack, bufferSource, OUTPUT_OVERLAY, direction, brightness);
                }
            }
        }
    }

    private void renderOverlay(PoseStack poseStack, MultiBufferSource bufferSource, ResourceLocation texture, Direction face, int packedLight) {

        // Задаем поворот текстуры в зависимости от стороны блока
        poseStack.pushPose();
        alignToFace(poseStack, face);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(texture));
        Matrix4f matrix = poseStack.last().pose();

        float size = 0.99f; // Размер наложенной текстуры
        float min = (1 - size) / 2;
        float max = 1 - min;
        float z = 0; // Позиция по оси Z для лицевой стороны
        poseStack.popPose();
        // Рисуем наложенную текстуру
        vertexConsumer.vertex(matrix, min, max, z).color(0xFFFFFFFF).uv(0, 1).overlayCoords(0, 0).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(matrix, max, max, z).color(0xFFFFFFFF).uv(1, 1).overlayCoords(0, 0).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(matrix, max, min, z).color(0xFFFFFFFF).uv(1, 0).overlayCoords(0, 0).uv2(packedLight).normal(0, 0, 1).endVertex();
        vertexConsumer.vertex(matrix, min, min, z).color(0xFFFFFFFF).uv(0, 0).overlayCoords(0, 0).uv2(packedLight).normal(0, 0, 1).endVertex();


    }

    private void alignToFace(PoseStack poseStack, Direction face) {
        switch (face) {
            case NORTH -> {} // по умолчанию, без изменений
            case SOUTH -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(180));
                poseStack.translate(0, -1, -1);
            } // переворачиваем на 180 градусов
            case WEST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(90));
                poseStack.translate(-1, 0, 0);
            } // поворачиваем на 90 градусов
            case EAST -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(-90));
                poseStack.translate(0, 0, -1);
            } // поворачиваем на -90 градусов
            case UP -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(90));
                poseStack.translate(0, 0, -1);
            } // поворачиваем на -90 градусов по оси X
            case DOWN -> {
                poseStack.mulPose(Axis.XP.rotationDegrees(270));
                poseStack.translate(0, -1, 0);
            } // поворачиваем на 90 градусов по оси X
            default -> {}
        }

        // Плавно смещаем текстуру немного вперёд, чтобы избежать артефактов с наложением на другие поверхности.
        poseStack.translate(-0.0001, -0.0001, -0.0001);
    }

    private boolean isInput(T blockEntity, Direction face) {
        // Твоя логика проверки (например, данные из BlockEntity)
        return blockEntity instanceof BEBlockEntity myBlock && myBlock.getSideMode(face) == EnergySide.INPUT;
    }

    private boolean isOutput(T blockEntity, Direction face) {
        return blockEntity instanceof BEBlockEntity myBlock && myBlock.getSideMode(face) == EnergySide.OUTPUT;
    }
}
