package com.alko.alkomod.screen;

import com.alko.alkomod.Alkomod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CounterScreen extends AbstractContainerScreen<CounterMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Alkomod.MOD_ID, "textures/gui/screen/yandex_bag_screen.png");
    int storedEnergy;
    int capacity;
    int x;
    int y;

    public CounterScreen(CounterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.storedEnergy = menu.storedEnergy;
        this.capacity = menu.capacity;
    }

    @Override
    protected void init() {
        super.init();
        x = (width - imageWidth) / 2;
        y = (height - imageHeight) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, this.x, this.y, 0, 0, this.imageWidth, this.imageHeight);
        renderBar(guiGraphics);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void renderBar(GuiGraphics pGuiGraphics){
        int energyCount = menu.getScaledEneryCount();
        int u = 176;
        int v = 0;
        pGuiGraphics.blit(TEXTURE, this.x+143, this.y+76-energyCount, u, v, 20, energyCount);
    }
}
