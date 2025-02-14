package com.alko.alkomod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class Keybindings {
    public static final Keybindings INSTANCE = new Keybindings();

    private Keybindings() {}

    private static final String CATEGORY = "key.categories." + Alkomod.MOD_ID;

    public final KeyMapping OpenBagKey = new KeyMapping(
            "key." + Alkomod.MOD_ID + ".open_bag_key",
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_B, -1),
            CATEGORY
    );
}
