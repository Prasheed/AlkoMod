package com.alko.alkomod.events;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.entity.ModEntities;
import com.alko.alkomod.entity.custom.PotbellyEntity;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.POTBELLY.get(), PotbellyEntity.setAttributes());
    }
}