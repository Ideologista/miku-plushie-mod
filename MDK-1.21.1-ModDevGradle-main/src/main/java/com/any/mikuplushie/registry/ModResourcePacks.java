package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import net.minecraft.resources.ResourceLocation;

public class ModResourcePacks {

    public static void initialize() {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Resource Packs");
    }

    public static ResourceLocation asId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MikuPlushie.MOD_ID, path);
    }
}