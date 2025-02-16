package com.alko.alkomod.block.blockentity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.block.BEBattery;
import com.alko.alkomod.block.GeneratorBlock;
import com.alko.alkomod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntity {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Alkomod.MOD_ID);

    public static final RegistryObject<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("animated_block_entity", () ->
                    BlockEntityType.Builder.of(GeneratorBlockEntity::new,
                            ModBlocks.GENERATOR_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BEBatteryBlockEntity>> BE_BATTERY_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("be_battery_block_entity", () ->
                    BlockEntityType.Builder.of(BEBatteryBlockEntity::new,
                            ModBlocks.BE_BATTERY_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BEGeneratorBlockEntity>> BE_GENERATOR_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("be_generator_block_entity", () -> BlockEntityType.Builder.of(BEGeneratorBlockEntity::new, ModBlocks.BE_GENERATOR_BLOCK.get()).build(null));

}


