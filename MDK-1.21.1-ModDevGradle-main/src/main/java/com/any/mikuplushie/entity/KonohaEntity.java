package com.any.mikuplushie.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public class KonohaEntity extends AbstractPlushEntity {
    public KonohaEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
}