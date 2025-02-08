package com.alko.alkomod;

import com.alko.alkomod.Items.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CreativeTab {

    public static final ResourceLocation BACKGROUND = new ResourceLocation(Alkomod.MOD_ID,"textures/creative_tab_background.png");

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Alkomod.MOD_ID);

    public static final List<Supplier<? extends ItemLike>> ALKOMOD_TAB_ITEMS = new ArrayList<>();

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = TABS.register("alkomod_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.alkomod.tab"))
                    .icon(ModItems.URA_DEBUG.get()::getDefaultInstance)
                    .displayItems((displayParams, output) ->
                            ALKOMOD_TAB_ITEMS.forEach(itemLike -> output.accept(itemLike.get())))
                    .withSearchBar()
                    .withSlotColor(new Color(240, 132, 50, 100).getRGB())
                    .withBackgroundLocation(BACKGROUND)
                    .build()
    );


    public static <T extends Item> RegistryObject<T> addToTab(RegistryObject<T> itemLike) {
        ALKOMOD_TAB_ITEMS.add(itemLike);
        return itemLike;
    }
}
