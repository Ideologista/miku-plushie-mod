package com.any.mikuplushie.entity;

import com.any.mikuplushie.entity.goals.EatLeekGoal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public class MikuEntity extends AbstractPlushEntity {

    private boolean isEatingLeek = false;

    public MikuEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        // 1. Carica tutti i comportamenti di base (seguirti, attaccare, ecc.) definiti in AbstractPlushEntity
        super.registerGoals();
        // 2. Aggiunge l'obiettivo specifico di cercare e mangiare i porri SOLO a Miku
        this.goalSelector.addGoal(3, new EatLeekGoal(this));
    }

    // Metodo richiesto da EatLeekGoal per cambiare lo stato interno
    public void setEatingLeek(boolean eating) {
        this.isEatingLeek = eating;
    }

    // Metodo richiesto da EatLeekGoal quando il blocco di porro viene rotto/mangiato
    public void onEatingGrass() {
        // In Vanilla questo metodo avvia l'animazione delle pecore.
        // Visto che la guarigione (heal) la fai già all'interno del Goal, qui puoi lasciarlo vuoto,
        // oppure in futuro aggiungere logiche custom (es. particelle o trigger per GeckoLib).
    }
}