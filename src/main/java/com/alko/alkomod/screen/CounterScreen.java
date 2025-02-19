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
    private final CounterMenu menu;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Alkomod.MOD_ID, "textures/gui/screen/yandex_bag_screen.png");


    public CounterScreen(CounterMenu menu, Inventory playerInventory, Component title) {
        super(menu,playerInventory,title);
        this.menu = menu;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        int count = menu.getCountValue();
        int someOtherValue = menu.getSomeOtherValue();

        int centerX = this.width / 2;
        int centerY = this.height / 2;
        graphics.drawString(this.font, "Число: " + count, centerX - this.font.width("Число: " + count) / 2, centerY, 0xFFFFFF);
        graphics.drawString(this.font, "Другое значение: " + someOtherValue, centerX - this.font.width("Другое значение: " + someOtherValue) / 2, centerY + 20, 0xFFFFFF);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Координаты экрана, чтобы правильно отобразить GUI по центру
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Рисуем текстуру (заранее загруженную) в нужном месте
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Чтобы игра не ставилась на паузу
    }
}
