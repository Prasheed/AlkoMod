package com.alko.alkomod.entity;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.entity.custom.PotbellyEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Alkomod.MOD_ID);

    public static final RegistryObject<EntityType<PotbellyEntity>> POTBELLY =
            ENTITY_TYPES.register("potbelly",
                    () -> EntityType.Builder.of(PotbellyEntity::new, MobCategory.CREATURE)
                            .sized(1.5f, 1.75f)
                            .build(new ResourceLocation(Alkomod.MOD_ID, "potbelly").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
