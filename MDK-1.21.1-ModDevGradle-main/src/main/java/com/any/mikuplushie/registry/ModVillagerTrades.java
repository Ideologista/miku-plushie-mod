package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@EventBusSubscriber(modid = MikuPlushie.MOD_ID)
public class ModVillagerTrades {

    public static void initialize() {
        MikuPlushie.LOGGER.info("Registering " + MikuPlushie.MOD_ID + " Villager Trades");
    }

    @SubscribeEvent
    public static void addVillagerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.FARMER) {
            var trades = event.getTrades().get(1);

            // BUY LEEK SEEDS
            trades.add((entity, random) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ModItems.LEEK_SEEDS.get(), 3),
                    6, 2, 0.02f
            ));

            // SELL LEEK
            trades.add((entity, random) -> new MerchantOffer(
                    new ItemCost(ModItems.LEEK.get(), 16),
                    new ItemStack(Items.EMERALD, 1),
                    6, 2, 0.02f
            ));
        }
    }
}