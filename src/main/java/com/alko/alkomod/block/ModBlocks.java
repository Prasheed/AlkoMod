package com.alko.alkomod.block;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.CreativeTab;
import com.alko.alkomod.Items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Alkomod.MOD_ID);

    public static final RegistryObject<Block> GENERATOR_BLOCK = BLOCKS.register("generator_block", GeneratorBlock::new);

    public static final RegistryObject<Block> BASIC_CABLE_BLOCK = registerBlock("cable_block", CableBlock::new);
    public static final RegistryObject<Block> BE_GENERATOR_BLOCK = registerBlock("be_generator_block",
            BEGeneratorBlock::new);

    public static final RegistryObject<Block> BE_BATTERY_BLOCK = registerBlock("be_battery_block",
            BEBattery::new);
    //public static final RegistryObject<Block> SIMPLE_ENERGY_GENERATOR_BLOCK = registerBlock("simple_energy_generator_block",
    //        SimpleEnergyGeneratorBlock::new);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return CreativeTab.addToTab(ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties())));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
