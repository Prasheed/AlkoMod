package com.alko.alkomod.screen;

import com.alko.alkomod.Alkomod;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Alkomod.MOD_ID);

    public static final RegistryObject<MenuType<BEGeneratorBlockMenu>> BE_GENERATOR_BLOCK_MENU =
            registerMenuType("be_generator_block_menu", BEGeneratorBlockMenu::new);

    public static final RegistryObject<MenuType<BEBatteryMenu>> BE_BATTERY_MENU =
            registerMenuType("be_battery_menu", BEBatteryMenu::new);

    public static final RegistryObject<MenuType<CounterMenu>> COUNTER_MENU =
            registerMenuType("counter_menu", CounterMenu::new);



    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }


    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
