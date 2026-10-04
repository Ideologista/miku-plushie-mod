package com.any.mikuplushie.util;

import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModUtil {

    public static String getBlockIdFromBlockPos(Level level, BlockPos pos){
        return level.getBlockState(pos).getBlock().getDescriptionId().split("[.]")[2];
    }

    public static String getBlockIdFromBlockState(BlockState state){
        return state.getBlock().getDescriptionId().split("[.]")[2];
    }

    public static String getBlockIdFromItem(Item itemConvertible){
        return itemConvertible.getDescriptionId().split("[.]")[2];
    }

    public static String getBlockIdFromBlock(Block block){
        return block.getDescriptionId().split("[.]")[2];
    }

    public static String getEntityNameFromBlockId(String blockId){
        String[] blockIdWords = blockId.split("_");
        return blockIdWords[0] + "_" + blockIdWords[1];
    }

    public static String getFirstNameFromBlockId(String blockId){
        String[] blockIdWords = blockId.split("_");
        return blockIdWords[0];
    }

    public static SoundEvent getPlushSoundEvent(String plushName, String action){
        String firstName = getFirstNameFromBlockId(plushName);

        for (DeferredHolder<SoundEvent, SoundEvent> holder : ModSoundEvents.MIKU_PLUSHIES_SOUND_EVENTS){
            SoundEvent soundEvent = holder.get();
            String soundEventId = soundEvent.getLocation().getPath();
            if (soundEventId.contains(firstName) && soundEventId.contains("_" + action)){
                return soundEvent;
            }
        }

        return ModSoundEvents.MIKU_PLUSHIES_SOUND_EVENTS.getFirst().get();
    }

    public static void playPlushSound(Level level, BlockPos position, String plushName, String action){
        if (!plushName.equals(getBlockIdFromBlock(ModBlocks.KONOHA_PLUSH.get()))){
            SoundEvent soundEvent = getPlushSoundEvent(plushName, action);
            level.playSound(null, position, soundEvent, SoundSource.BLOCKS, 0.5F, 1F);
        }
    }
}