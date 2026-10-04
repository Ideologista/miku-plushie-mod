package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.item.MikuPlushieBlockItem;
import com.any.mikuplushie.item.ModFoodComponents;
import com.any.mikuplushie.item.PlushToolMaterial;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MikuPlushie.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MikuPlushie.MOD_ID);

    public static final List<DeferredItem<Item>> REGULAR_ITEMS = new ArrayList<>();
    public static final List<DeferredItem<Item>> PLUSH_ITEMS = new ArrayList<>();
    public static final List<DeferredItem<Item>> PICKAXE_ITEMS = new ArrayList<>();

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MIKU_GROUP = CREATIVE_MODE_TABS.register("item_group", () -> CreativeModeTab.builder()
            .title(Component.translatable("item.group.miku_plushies"))
            .icon(() -> new ItemStack(ModBlocks.MIKU_PLUSH.get()))
            .displayItems((parameters, output) -> {
                REGULAR_ITEMS.forEach(item -> output.accept(item.get()));
                PLUSH_ITEMS.forEach(item -> output.accept(item.get()));
                PICKAXE_ITEMS.forEach(item -> output.accept(item.get()));
            }).build());

    public static final DeferredItem<Item> CANUDINHO = registerRegular("canudinho", new Item.Properties().rarity(Rarity.RARE));
    public static final DeferredItem<Item> BAGUETTE = registerRegular("baguette", new Item.Properties().food(ModFoodComponents.BAGUETTE));
    public static final DeferredItem<Item> LEEK_SEEDS = registerRegular("leek_seeds", props -> new ItemNameBlockItem(ModBlocks.LEEK_CROP.get(), props));
    public static final DeferredItem<Item> LEEK = registerRegular("leek", new Item.Properties().food(ModFoodComponents.LEEK));
    public static final DeferredItem<Item> AKITA_NERU_PHONE = registerRegular("akita_neru_phone", new Item.Properties());
    public static final DeferredItem<Item> VOCALOID_HEART = registerRegular("vocaloid_heart", new Item.Properties());

    public static final DeferredItem<Item> TETO_PICKAXE = registerPickaxe("teto_pickaxe");
    public static final DeferredItem<Item> TETO_PICKAXE_MESMERIZER = registerPickaxe("teto_pickaxe_mesmerizer");
    public static final DeferredItem<Item> TETO_PICKAXE_BIRDBRAIN = registerPickaxe("teto_pickaxe_birdbrain");
    public static final DeferredItem<Item> TETO_PICKAXE_REGRET_ROCK = registerPickaxe("teto_pickaxe_regret_rock");
    public static final DeferredItem<Item> TETO_PICKAXE_DONT_BELIEVE_IN_T = registerPickaxe("teto_pickaxe_dont_believe_in_t");
    public static final DeferredItem<Item> TETO_PICKAXE_LIAR_DANCER = registerPickaxe("teto_pickaxe_liar_dancer");
    public static final DeferredItem<Item> TETO_PICKAXE_WHATCHACALLITSNAME = registerPickaxe("teto_pickaxe_whatchacallitsname");
    public static final DeferredItem<Item> TETO_PICKAXE_SOME_MORE_OF_THAT_SONG = registerPickaxe("teto_pickaxe_some_more_of_that_song");
    public static final DeferredItem<Item> TETO_PICKAXE_SYNTHV = registerPickaxe("teto_pickaxe_synthv");
    public static final DeferredItem<Item> TETO_PICKAXE_SPOKEN_FOR = registerPickaxe("teto_pickaxe_spoken_for");
    public static final DeferredItem<Item> TETO_PICKAXE_PPPP = registerPickaxe("teto_pickaxe_pppp");

    private static DeferredItem<Item> registerRegular(String name, Item.Properties properties) {
        DeferredItem<Item> registeredItem = ITEMS.registerSimpleItem(name, properties);
        REGULAR_ITEMS.add(registeredItem);
        return registeredItem;
    }

    private static DeferredItem<Item> registerRegular(String name, Function<Item.Properties, Item> factory) {
        DeferredItem<Item> registeredItem = ITEMS.registerItem(name, factory);
        REGULAR_ITEMS.add(registeredItem);
        return registeredItem;
    }

    private static DeferredItem<Item> registerPickaxe(String name) {
        DeferredItem<Item> registeredItem = ITEMS.registerItem(name, props -> new PickaxeItem(PlushToolMaterial.PLUSH_TOOL_MATERIAL, props.attributes(PickaxeItem.createAttributes(PlushToolMaterial.PLUSH_TOOL_MATERIAL, 1f, -2.8F))));
        PICKAXE_ITEMS.add(registeredItem);
        return registeredItem;
    }

    public static DeferredItem<Item> registerPlush(String name, Supplier<Block> blockSupplier) {
        DeferredItem<Item> registeredItem = ITEMS.registerItem(name, props -> new MikuPlushieBlockItem(blockSupplier.get(), props));
        PLUSH_ITEMS.add(registeredItem);
        return registeredItem;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
    }
}