package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.entity.AbstractPlushEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModSoundEvents {
	private ModSoundEvents() {}

	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, MikuPlushie.MOD_ID);

	public static final List<DeferredHolder<SoundEvent, SoundEvent>> MIKU_PLUSHIES_SOUND_EVENTS = new ArrayList<>();
	protected static final List<String> SOUND_EVENT_SUFFIXES = List.of("oie", "dor", "bye", "equip");
	protected static final List<String> MIKU_SOUND_EVENT_SUFFIXES = List.of("canudinho", "eat");

	private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String id) {
		DeferredHolder<SoundEvent, SoundEvent> sound = SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MikuPlushie.MOD_ID, id)));
		MIKU_PLUSHIES_SOUND_EVENTS.add(sound);
		return sound;
	}

	public static void register(IEventBus eventBus) {
		MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Sounds");

		for (var plush : ModEntities.PLUSH_ENTITIES) {

			// SKIP KONOHA PLUSH
			if (!plush.equals(ModEntities.KONOHA)) {
				String baseName = plush.getId().getPath().replace("_plush", "");

				// ADD MIKU SOUND EVENTS
				if (plush.equals(ModEntities.MIKU)) {
					for (String mikuSoundEvent : MIKU_SOUND_EVENT_SUFFIXES) {
						registerSound(baseName + "_" + mikuSoundEvent);
					}
				}

				// ADD REGULAR SOUND EVENTS
				for (String event : SOUND_EVENT_SUFFIXES) {
					registerSound(baseName + "_" + event);
				}
			}
		}

		SOUND_EVENTS.register(eventBus);
	}
}