package com.alko.alkomod;

import com.alko.alkomod.Items.ModItems;
import com.alko.alkomod.block.ModBlocks;
import com.alko.alkomod.block.blockentity.ModBlockEntity;
import com.alko.alkomod.block.blockentity.client.GeneratorBlockRenderer;
import com.alko.alkomod.block.blockentity.client.UniversalBEBlockEntityRenderer;
import com.alko.alkomod.entity.ModEntities;
import com.alko.alkomod.entity.client.PotbellyRenderer;
import com.alko.alkomod.handlers.PlayerInputHandler;
import com.alko.alkomod.screen.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Alkomod.MOD_ID)
public class Alkomod
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "alko";
    // Directly reference a slf4j logger

    private static final Logger LOGGER = LogUtils.getLogger();


    public Alkomod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        ModBlockEntity.BLOCK_ENTITIES.register(modEventBus);
        CreativeTab.TABS.register(modEventBus);
        GeckoLib.initialize();
        ModMenuTypes.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new PlayerInputHandler());

    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Server started MisterTwister");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            MenuScreens.register(ModMenuTypes.BE_GENERATOR_BLOCK_MENU.get(), BEGeneratorBlockScreen::new);
            MenuScreens.register(ModMenuTypes.BE_BATTERY_MENU.get(), BEBatteryScreen::new);
            MenuScreens.register(ModMenuTypes.COUNTER_MENU.get(), CounterScreen::new);
            MenuScreens.register(ModMenuTypes.FLUID_TANK_MENU.get(), FluidTankBlockScreen::new);
            BlockEntityRenderers.register(ModBlockEntity.GENERATOR_BLOCK_ENTITY.get(), GeneratorBlockRenderer::new);
            EntityRenderers.register(ModEntities.POTBELLY.get(), PotbellyRenderer::new);
        }

        @SubscribeEvent
        public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntity.BE_GENERATOR_BLOCK_ENTITY.get(), UniversalBEBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(ModBlockEntity.BE_BATTERY_BLOCK_ENTITY.get(), UniversalBEBlockEntityRenderer::new);
        }
    }
}
