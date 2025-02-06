package com.alko.alkomod.events;


import com.alko.alkomod.Alkomod;
import com.alko.alkomod.network.PacketHandler;
import com.google.common.eventbus.Subscribe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonEvents {




    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event){
        event.enqueueWork(PacketHandler::register);
    }
}
