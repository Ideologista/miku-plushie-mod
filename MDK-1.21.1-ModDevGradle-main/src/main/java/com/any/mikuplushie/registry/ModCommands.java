package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.commands.SpawnMikusCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommands {

    public static void initialize() {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Commands");
        NeoForge.EVENT_BUS.register(ModCommands.class);
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        SpawnMikusCommand.register(event.getDispatcher());
    }
}