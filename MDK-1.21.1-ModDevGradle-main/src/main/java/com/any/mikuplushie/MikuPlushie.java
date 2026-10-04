package com.any.mikuplushie;

import com.any.mikuplushie.entity.variant.PlushVariants;
import com.any.mikuplushie.registry.*;
import com.any.mikuplushie.worldgen.generators.ModWorldGeneration;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(MikuPlushie.MOD_ID)
public class MikuPlushie {
	public static final String MOD_ID = "mikuplushie";
	public static final Logger LOGGER = LogUtils.getLogger();

	public MikuPlushie(IEventBus modEventBus, ModContainer modContainer) {
		LOGGER.info("Miku is now Joining NeoForge!!!");

		ModBlocks.register(modEventBus);
		ModItems.register(modEventBus);
		ModEntities.register(modEventBus);
		ModSoundEvents.register(modEventBus);
		ModParticles.register(modEventBus);

		ModCommands.initialize();
		ModResourcePacks.initialize();
		ModVillagerTrades.initialize();
		ModWorldGeneration.generateModWorldGeneration();

		modEventBus.addListener(this::commonSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			PlushVariants.initialize();
		});

		LOGGER.info("Miku: (^v^)/ Hi!!!");
	}
}