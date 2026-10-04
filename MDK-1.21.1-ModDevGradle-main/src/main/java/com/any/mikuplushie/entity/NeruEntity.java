package com.any.mikuplushie.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public class NeruEntity extends AbstractPlushEntity {
    public NeruEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
}