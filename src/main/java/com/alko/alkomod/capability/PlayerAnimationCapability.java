package com.alko.alkomod.capability;

import net.minecraft.nbt.CompoundTag;

import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationCapability {
    private Map<String, String> animationStateMap = new HashMap<>();

    public Map<String, String> getAnimationStateMap() {
        return animationStateMap;
    }

    public String getAnimationState(String  key){
        return animationStateMap.get(key);
    }

    public void setAnimationState(String key, String value){
        animationStateMap.put(key, value);

    }

    public void copyFrom(PlayerAnimationCapability source){
        this.animationStateMap = source.animationStateMap;
    }

    public void saveNBTdata(CompoundTag nbt){
        animationStateMap.forEach(nbt::putString);
    }

    public void loadNBTdata(CompoundTag nbt){
        animationStateMap.clear();
        nbt.getAllKeys().forEach(key -> {
            animationStateMap.put(key, nbt.getString(key));
        });
    }
}
