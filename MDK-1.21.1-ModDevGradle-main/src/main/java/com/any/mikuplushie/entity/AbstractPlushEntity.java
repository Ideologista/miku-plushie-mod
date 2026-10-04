package com.any.mikuplushie.entity;

import com.any.mikuplushie.entity.goals.MikuDelayedAttackGoal;
import com.any.mikuplushie.entity.variant.PlushVariants;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.registry.ModEntities;
import com.any.mikuplushie.registry.ModItems;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class AbstractPlushEntity extends TamableAnimal implements GeoEntity {

    private static final EntityDataAccessor<Integer> SPAWN_AGE = SynchedEntityData.defineId(AbstractPlushEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(AbstractPlushEntity.class, EntityDataSerializers.INT);

    // DANCE GLOBALS
    boolean songPlaying;
    @Nullable BlockPos songSource;

    // GECKOLIB VARIABLES
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("misc.idle");
    public static final RawAnimation SIT = RawAnimation.begin().thenLoop("misc.sit");
    public static final RawAnimation SIT_DANCE = RawAnimation.begin().thenLoop("misc.sit-dance");
    protected static final List<RawAnimation> ATTACK_ANIMATIONS = List.of(
            RawAnimation.begin().thenPlay("attack.swipe"),
            RawAnimation.begin().thenPlay("attack.swipe2"),
            RawAnimation.begin().thenPlay("attack.swipe3")
    );
    public static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("misc.spawn");

    protected AbstractPlushEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    // GOALS
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new MikuDelayedAttackGoal(this, 1.5F, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 5F, 1F ));
        this.goalSelector.addGoal(6, new TemptGoal(this, 1.5, Ingredient.of(ModItems.LEEK.get()), false));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, AbstractPlushEntity.class, 8F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return !(target instanceof AbstractPlushEntity)
                && !(target instanceof Creeper)
                && !(target instanceof Ghast);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 2.0F);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public List<RawAnimation> getDances() {
        return List.of(
                RawAnimation.begin().thenLoop("misc.dance.generic.caramelldansen")
        );
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "Plush", 2, state -> {
            List<RawAnimation> DANCES = getDances();

            if (this.isInSittingPose()) {
                if (this.isSongPlaying()) {
                    return state.setAndContinue(SIT_DANCE);
                } else {
                    return state.setAndContinue(SIT);
                }
            } else {
                if (this.entityData.get(SPAWN_AGE) < 10) {
                    return state.setAndContinue(SPAWN);
                } else if (this.isSongPlaying()) {
                    RawAnimation currentAnimation = state.getController().getCurrentRawAnimation();
                    for (RawAnimation animation : DANCES) {
                        if (currentAnimation != null && currentAnimation.equals(animation)) {
                            return state.setAndContinue(animation);
                        }
                    }
                    if (DANCES.size() == 1) {
                        return state.setAndContinue(DANCES.get(0));
                    } else {
                        return state.setAndContinue(DANCES.get(this.random.nextInt(DANCES.size())));
                    }
                } else if (this.swinging) {
                    RawAnimation currentAnimation = state.getController().getCurrentRawAnimation();
                    for (RawAnimation animation : ATTACK_ANIMATIONS) {
                        if (currentAnimation != null && currentAnimation.equals(animation)) {
                            return state.setAndContinue(animation);
                        }
                    }
                    return state.setAndContinue(ATTACK_ANIMATIONS.get(this.random.nextInt(ATTACK_ANIMATIONS.size())));
                } else {
                    return state.setAndContinue(IDLE);
                }
            }
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getCurrentSwingDuration() {
        return 10;
    }

    @Override
    public float getVoicePitch() {
        return 1F;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        String plushName = this.getType().getDescriptionId().split("[.]")[2];
        return ModUtil.getPlushSoundEvent(plushName, "bye");
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResult.PASS;
        }

        ItemStack playerItemStack = player.getItemInHand(hand);
        FoodProperties foodComponent = playerItemStack.get(DataComponents.FOOD);
        ItemStack entityHandStack = this.getMainHandItem();

        // 1. ADDOMESTICAZIONE
        if (!this.isTame()) {
            if (!this.level().isClientSide) {
                this.tame(player);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        // 2. AZIONI DEL PROPRIETARIO
        if (this.isTame() && this.isOwnedBy(player)) {

            // CURA COL PORRO
            if (this.getHealth() < this.getMaxHealth() && playerItemStack.is(ModItems.LEEK.get())) {
                if (!this.level().isClientSide) {
                    if (!player.getAbilities().instabuild) {
                        playerItemStack.shrink(1);
                    }
                    float nutrition = foodComponent != null ? (float) foodComponent.nutrition() : 1.0F;
                    this.heal(nutrition);
                    this.playSound(SoundEvents.GENERIC_EAT, 1, 1);
                } else {
                    for (int particle = 0; particle < 20; particle++) {
                        this.level().addParticle(
                                new BlockParticleOption(ParticleTypes.BLOCK, ModBlocks.LEEK_CROP.get().defaultBlockState()),
                                this.getX(), this.getY() + 0.5D, this.getZ(),
                                this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.1, this.random.nextGaussian() * 0.1
                        );
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            // DARE L'ARMA (Se il giocatore ha in mano un'arma compatibile e il peluche è disarmato)
            else if (!playerItemStack.isEmpty() && this.canHoldItem(playerItemStack) && entityHandStack.isEmpty()) {
                if (!this.level().isClientSide) {
                    ItemStack swordToGive = playerItemStack.copy();
                    swordToGive.setCount(1);
                    this.setItemSlot(EquipmentSlot.MAINHAND, swordToGive);

                    if (!player.getAbilities().instabuild) {
                        playerItemStack.shrink(1);
                    }
                    this.playSound(SoundEvents.ARMOR_EQUIP_GENERIC.value(), 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }

            // A MANI VUOTE (Disarma o Alzati/Siediti)
            else if (playerItemStack.isEmpty()) {

                // SHIFT + CLICK: Riprenditi l'arma
                if (player.isShiftKeyDown() && !entityHandStack.isEmpty()) {
                    if (!this.level().isClientSide) {
                        this.spawnAtLocation(entityHandStack);
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }

                // CLICK NORMALE: Alzati o Siediti
                else if (!player.isShiftKeyDown()) {
                    if (!this.level().isClientSide) {
                        boolean willSit = !this.isOrderedToSit();
                        this.setOrderedToSit(willSit);
                        this.setInSittingPose(willSit);

                        this.jumping = false;
                        this.navigation.stop();
                        this.setTarget(null);
                    }
                    return InteractionResult.sidedSuccess(this.level().isClientSide);
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return stack.is(ItemTags.SWORDS);
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.updateSwingTime();

        if (this.songSource == null
                || !this.songSource.closerToCenterThan(this.position(), 8D)
                || !this.level().getBlockState(this.songSource).is(Blocks.JUKEBOX)) {
            this.songPlaying = false;
            this.songSource = null;
        }

        if (this.entityData.get(SPAWN_AGE) < 10) {
            this.entityData.set(SPAWN_AGE, Math.min(this.tickCount, 10));
        }
    }

    public boolean isSongPlaying() {
        return this.songPlaying;
    }

    @Override
    public void setRecordPlayingNearby(BlockPos songPosition, boolean playing) {
        this.songSource = songPosition;
        this.songPlaying = playing;
    }

    public String getPlushName() {
        return this.getType().getDescriptionId().split("[.]")[2].split("'")[0];
    }

    protected List<String> getVariantList() {
        List<String> variantList = null;
        for (List<String> variants : PlushVariants.ALL_PLUSH_VARIANTS) {
            if (variants.contains(this.getPlushName())) {
                variantList = variants;
            }
        }
        return variantList;
    }

    protected EntityDataAccessor<Integer> getVariantDataTracker() {
        return VARIANT;
    }

    private int getTrackedVariant() {
        return this.entityData.get(this.getVariantDataTracker());
    }

    public String getVariant() {
        return this.getVariantList().get(this.entityData.get(this.getVariantDataTracker()));
    }

    public void setVariant(Integer variant) {
        this.entityData.set(this.getVariantDataTracker(), variant);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SPAWN_AGE, 0);
        builder.define(this.getVariantDataTracker(), 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(SPAWN_AGE, nbt.getInt("SpawnAge"));
        this.entityData.set(this.getVariantDataTracker(), nbt.getInt("Variant"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("SpawnAge", Math.min(this.tickCount, 11));
        nbt.putInt("Variant", this.getTrackedVariant());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    public void setVariantByBlock(String variant) {
        for (int variation = 0; variation < this.getVariantList().size(); variation++) {
            if (this.getVariantList().get(variation).equals(variant)) {
                this.entityData.set(this.getVariantDataTracker(), variation);
            }
        }
    }
}