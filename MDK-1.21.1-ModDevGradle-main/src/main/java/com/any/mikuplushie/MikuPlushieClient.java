package com.any.mikuplushie;

import com.any.mikuplushie.entity.client.render.AbstractPlushRender;
import com.any.mikuplushie.particle.PlushSpawnParticle;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModParticles;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import software.bernie.geckolib.loading.math.MathParser;

// Corretto il commento: in 1.21.1 gli eventi vengono smistati in automatico in base alla classe!
@EventBusSubscriber(modid = MikuPlushie.MOD_ID, value = Dist.CLIENT)
public class MikuPlushieClient {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        MikuPlushie.LOGGER.info("Initializing " + MikuPlushie.MOD_ID + " Client Setup");

        // GLIB QUERY
        MathParser.setVariable("query.miku.is_game", () -> 90 / Math.PI);

        // FIX TRASPARENZA BLOCCHI: Forza il gioco a supportare il cutout
        event.enqueueWork(() -> {
            // NOTA: Se il tuo registro dei blocchi non si chiama "BLOCKS",
            // ma ad esempio "PLUSH_BLOCKS", cambialo in questa riga qui sotto.
            for (var block : ModBlocks.BLOCKS.getEntries()) {
                ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.cutout());
            }
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        // ENTITIES RENDERERS
        for (var plushEntityHolder : ModEntities.PLUSH_ENTITIES) {
            event.registerEntityRenderer(plushEntityHolder.get(), AbstractPlushRender::new);
        }
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        // PARTICLE
        event.registerSpriteSet(ModParticles.MIKU_SPAWN.get(), PlushSpawnParticle.Factory::new);
    }
}