package com.any.mikuplushie.registry;

import com.any.mikuplushie.MikuPlushie;
import com.any.mikuplushie.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MikuPlushie.MOD_ID);

    public static final List<DeferredHolder<EntityType<?>, ? extends EntityType<? extends AbstractPlushEntity>>> PLUSH_ENTITIES = new ArrayList<>();

    public static final float PLUSH_WIDTH = 0.6F;
    public static final float PLUSH_HEIGHT = 1F;

    public static final DeferredHolder<EntityType<?>, EntityType<MikuEntity>> MIKU = registerMob("miku_plush", MikuEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<TetoEntity>> TETO = registerMob("teto_plush", TetoEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<AikoEntity>> AIKO = registerMob("aiko_plush", AikoEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<NeruEntity>> NERU = registerMob("akita_neru_plush", NeruEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<RinEntity>> RIN = registerMob("rin_plush", RinEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<LenEntity>> LEN = registerMob("len_plush", LenEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<KonohaEntity>> KONOHA = registerMob("konoha_plush", KonohaEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<LukaEntity>> LUKA = registerMob("luka_plush", LukaEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<MeikoEntity>> MEIKO = registerMob("meiko_plush", MeikoEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<GumiEntity>> GUMI = registerMob("gumi_plush", GumiEntity::new);
    public static final DeferredHolder<EntityType<?>, EntityType<KaitoEntity>> KAITO = registerMob("kaito_plush", KaitoEntity::new);

    private static <T extends AbstractPlushEntity> DeferredHolder<EntityType<?>, EntityType<T>> registerMob(String name, EntityType.EntityFactory<T> factory) {
        DeferredHolder<EntityType<?>, EntityType<T>> entityType = ENTITY_TYPES.register(name, () ->
                EntityType.Builder.of(factory, MobCategory.CREATURE)
                        .sized(PLUSH_WIDTH, PLUSH_HEIGHT)
                        .eyeHeight(0.85F)
                        .build(name)
        );
        PLUSH_ENTITIES.add(entityType);
        return entityType;
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        for (DeferredHolder<EntityType<?>, ? extends EntityType<? extends AbstractPlushEntity>> entity : PLUSH_ENTITIES) {
            event.put(entity.get(), AbstractPlushEntity.createAttributes().build());
        }
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(ModEntities::registerAttributes);
    }
}