package com.alko.alkomod.screen;

import com.alko.alkomod.Alkomod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BEGeneratorBlockScreen extends AbstractContainerScreen<BEGeneratorBlockMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Alkomod.MOD_ID, "textures/gui/screen/be_generator_block_screen.png");
    int x;
    int y;

    public BEGeneratorBlockScreen(BEGeneratorBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);


        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderEnergy(guiGraphics, x, y);
        renderLit(guiGraphics, x, y);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
        int energyBarX = x + 154; // Горизонтальная позиция индикатора
        int energyBarY = y + 6; // Вертикальная позиция индикатора
        int energyBarWidth = 15; // Ширина индикатора
        int energyBarHeight = 72; // Высота индикатора
        if(mouseX >= energyBarX && mouseX <= energyBarX + energyBarWidth && mouseY >= energyBarY && mouseY <= energyBarY + energyBarHeight){
            guiGraphics.renderTooltip(Minecraft.getInstance().font, Component.literal(menu.getStoredEnergy()+"/"+menu.getCapacity()), mouseX, mouseY);
        }
    }

    private void renderEnergy(GuiGraphics guiGraphics, int x, int y) {
        int energyHeight = menu.getScaledEnergyCount();
        int xPos = x + 154; // Горизонтальная позиция индикатора на экране
        int yPos = y + 6 + 72 - energyHeight; // Вертикальная позиция с учётом заполнения снизу вверх
        int u = 176; // Горизонтальная координата на текстуре
        int v = 14 + 72 - energyHeight; // Вертикальная координата на текстуре

        guiGraphics.blit(TEXTURE, xPos, yPos, u, v, 15, energyHeight);
    }

    private void renderLit(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isLit()) {
            int fireHeight = menu.getScaledLitProgress();
            int xPos = x + 27;
            int yPos = y + 39 + 13 - fireHeight; // Смещаем позицию отрисовки вверх
            int u = 176; // Горизонтальная позиция текстуры
            int v = 0 + 13 - fireHeight; // Вертикальная позиция текстуры
            guiGraphics.blit(TEXTURE, xPos, yPos, u, v, 15, fireHeight);
        }
    }
}
