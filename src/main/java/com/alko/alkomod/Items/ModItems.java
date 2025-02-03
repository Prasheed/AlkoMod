package com.alko.alkomod.Items;

import com.alko.alkomod.Alkomod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Alkomod.MOD_ID);

    public static final RegistryObject<Item> URA_DEBUG = ITEMS.register("ura_debug", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> FARE_DUMB = ITEMS.register("fare_dumb", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
