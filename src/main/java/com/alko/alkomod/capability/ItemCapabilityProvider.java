package com.alko.alkomod.capability;

import com.alko.alkomod.Alkomod;
import com.alko.alkomod.Items.YandexBag;
import com.alko.alkomod.util.CountData;
import com.alko.alkomod.util.ICountData;
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
    private final CountData countData = new CountData(); // Объект с нашими данными
    private final LazyOptional<ICountData> instance = LazyOptional.of(() -> countData);

    // Возвращаем Capability, если он запрашивается
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilitiesRegister.COUNT_DATA ? instance.cast() : LazyOptional.empty();
    }

    // Сохранение данных в NBT, чтобы они сохранялись в мире
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("count", countData.getCount()); // Сохраняем число
        return tag;
    }

    // Восстанавливаем данные из NBT
    @Override
    public void deserializeNBT(CompoundTag nbt) {
        countData.setCount(nbt.getInt("count"));
    }

    // Присоединяем Capability к предмету
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof YandexBag) { // Проверяем, что предмет - наш
            event.addCapability(new ResourceLocation(Alkomod.MOD_ID, "count_data"), new ItemCapabilityProvider());
        }
    }
}