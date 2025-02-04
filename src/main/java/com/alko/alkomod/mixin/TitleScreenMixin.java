package com.alko.alkomod.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(at = @At("TAIL"), method = "createNormalMenuOptions(II)V")
    private void createNormalMenuOptions(CallbackInfo info){
        List<Renderable> renderables = ((ScreenAccessorMixin) this).getRenderables();
        renderables.forEach(renderable -> {
            if(renderable instanceof Button button){
                if(button.getMessage().contains(Component.translatable("menu.singleplayer"))){
                    button.setMessage(Component.literal("Одиночная игра нахрен"));
                }
                if(button.getMessage().contains(Component.translatable("menu.multiplayer"))){
                    button.setMessage(Component.literal("Подсосаться"));
                }
            }
        });
    }
}
