package com.alko.alkomod.screen;

import com.alko.alkomod.Alkomod;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BEBatteryScreen extends AbstractContainerScreen<BEBatteryMenu> {
    int x;
    int y;
    private static final ResourceLocation gui = new ResourceLocation(Alkomod.MOD_ID, "textures/gui/screen/be_battery_screen.png");

    public BEBatteryScreen(BEBatteryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
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
        RenderSystem.setShaderTexture(0, gui);


        guiGraphics.blit(gui, x, y, 0, 0, imageWidth, imageHeight);
        renderBar(guiGraphics);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    private void renderBar(GuiGraphics pGuiGraphics){
        int energyCount = menu.getScaledEnergyCount();
        int xPos = x + 66;
        int yPos = y + 68 - energyCount;
        int u = 176;
        int v = 0;
        pGuiGraphics.blit(gui, xPos, yPos, u, v, 44, energyCount);
    }
}
