package com.alko.alkomod.Items;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.CreativeTab;
import com.alko.alkomod.Items.custom.GeneratorBlockItem;
import com.alko.alkomod.block.ModBlocks;
import com.alko.alkomod.entity.ModEntities;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    //CreativeTab.addToTab()

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Alkomod.MOD_ID);

    public static final RegistryObject<Item> URA_DEBUG = CreativeTab.addToTab(ITEMS.register("ura_debug", UraDebugItem::new));

    public static final RegistryObject<Item> FARE_DUMB = CreativeTab.addToTab(ITEMS.register("fare_dumb", FareItem::new));

    public static final RegistryObject<Item> BASE_WINGS = CreativeTab.addToTab(ITEMS.register("base_wings", () -> new WingsItem(new Item.Properties())));

    public static final RegistryObject<Item> GLIDER = CreativeTab.addToTab(ITEMS.register("glider", () -> new GliderItem(new Item.Properties())));

    public static final RegistryObject<Item> BATTERY_ITEM = CreativeTab.addToTab(ITEMS.register("battery_item", BatteryItem::new));

    public static final RegistryObject<Item> GENERATOR_BLOCK_ITEM = CreativeTab.addToTab(ITEMS.register("generator_block", () -> new GeneratorBlockItem(ModBlocks.GENERATOR_BLOCK.get(),new Item.Properties())));

    public static final RegistryObject<Item> POTBELLY_SPAWN_EGG = CreativeTab.addToTab(ITEMS.register("potbelly_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.POTBELLY, 0xD57E36, 0x1D0D00, new Item.Properties())));

    public static final RegistryObject<Item> HU_TAO = CreativeTab.addToTab(ITEMS.register("hu_tao", () -> new HuTao(new Item.Properties())));

    public static final RegistryObject<Item> WRENCH = CreativeTab.addToTab(ITEMS.register("wrench", () -> new WrenchItem(new Item.Properties())));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
