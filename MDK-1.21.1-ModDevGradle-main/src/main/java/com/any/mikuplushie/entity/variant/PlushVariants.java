package com.any.mikuplushie.entity.variant;

import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class PlushVariants {

    public static List<List<String>> ALL_PLUSH_VARIANTS = new ArrayList<>();

    public static void initialize() {

        // FOR EACH PLUSH ENTITY
        for (var entityHolder : ModEntities.PLUSH_ENTITIES) {

            // GET ENTITY REGISTRY NAME (in NeoForge estraiamo il percorso direttamente dall'ID del DeferredHolder)
            String plushName = entityHolder.getId().getPath();

            // CREATE SPECIFIC PLUSH VARIANTS LIST
            List<String> plushVariants = new ArrayList<>();

            // FOR EACH BLOCK IN THE LIST
            for (var blockHolder : ModBlocks.PLUSH_BLOCKS) {
                // Estraiamo il blocco effettivo dal DeferredBlock
                Block value = blockHolder.get();
                String blockId = ModUtil.getBlockIdFromBlock(value);

                // ADD IT TO SPECIFIC PLUSH VARIANTS LIST IF IT CONTAINS THE ENTITY NAME
                if (blockId.contains(plushName)) {
                    plushVariants.add(blockId);
                }
            }
            // ADD SPECIFIC PLUSH VARIANTS LIST TO MAIN LIST
            ALL_PLUSH_VARIANTS.add(plushVariants);
        }

    }
}