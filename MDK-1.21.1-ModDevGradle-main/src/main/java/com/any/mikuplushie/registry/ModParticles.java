package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, MikuPlushie.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> MIKU_SPAWN = PARTICLES.register("miku_spawn", () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Particles");
        PARTICLES.register(eventBus);
    }
}