package com.alko.alkomod.capability;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.YandexBag;
import com.alko.alkomod.util.IItemStackHandlerData;
import com.alko.alkomod.util.ItemStackHandlerData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Alkomod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    private final ItemStackHandlerData inventoryData = new ItemStackHandlerData(15); // 15 слотов (5x3)
    private final LazyOptional<IItemStackHandlerData> inventoryInstance = LazyOptional.of(() -> inventoryData);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilitiesRegister.INVENTORY_DATA ? inventoryInstance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("inventoryData", inventoryData.serializeNBT()); // Сохраняем инвентарь в NBT
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("inventoryData")) {
            inventoryData.deserializeNBT(nbt.getCompound("inventoryData")); // Загружаем инвентарь
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof YandexBag) {
            event.addCapability(new ResourceLocation(Alkomod.MOD_ID, "inventory_data"), new ItemCapabilityProvider());
        }
    }
}
