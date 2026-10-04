package com.any.mikuplushie.entity.goals;

import com.any.mikuplushie.blocks.LeekCropBlock;
import com.any.mikuplushie.entity.MikuEntity;
import com.any.mikuplushie.registry.ModBlocks;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumSet;
import java.util.List;

// EAT LEEK GOAL
public class EatLeekGoal extends Goal {

    private static final int MAX_TIMER = 40;
    private final MikuEntity miku;
    private final Level world;
    private int timer;

    public EatLeekGoal(MikuEntity miku) {
        this.miku = miku;
        this.world = miku.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    private BlockState getFullyGrownLeek() {
        return ModBlocks.LEEK_CROP.get().defaultBlockState().setValue(LeekCropBlock.AGE, 7);
    }

    @Override
    public boolean canUse() {
        BlockPos blockPos = this.miku.blockPosition();
        boolean adjacentToLeek = !locateFullyGrownLeek(blockPos).equals(blockPos);
        boolean healthNotMaxed = this.miku.getHealth() < this.miku.getMaxHealth();
        return adjacentToLeek && healthNotMaxed;
    }

    @Override
    public void start() {
        this.timer = this.adjustedTickDelay(MAX_TIMER);
        this.miku.lookAt(EntityAnchorArgument.Anchor.EYES, locateFullyGrownLeek(this.miku.blockPosition()).getCenter());
        this.world.broadcastEntityEvent(this.miku, (byte) 10); // 10 is the eat grass timer status
        this.miku.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.timer = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.timer > 0;
    }

    public int getTimer() {
        return this.timer;
    }

    @Override
    public void tick() {
        this.timer = Math.max(0, this.timer - 1);

        if (this.timer % 4 == 1 && this.timer > 4) {
            this.miku.playSound(SoundEvents.GENERIC_EAT, 0.5F, 1);
            this.miku.playSound(ModUtil.getPlushSoundEvent("miku_plush", "eat"), 1, 1);
        }

        if (this.timer == this.adjustedTickDelay(4)) {
            BlockPos mobPos = this.miku.blockPosition();
            BlockState currentState = this.world.getBlockState(mobPos);

            if (currentState.is(ModBlocks.LEEK_CROP.get()) && currentState.getValue(LeekCropBlock.AGE) == 7) {
                if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    this.world.destroyBlock(mobPos, false);
                }
                this.miku.onEatingGrass();

            } else {
                BlockPos blockPos2 = mobPos;

                if (!locateFullyGrownLeek(mobPos).equals(mobPos)) {
                    blockPos2 = locateFullyGrownLeek(mobPos);
                }

                if (this.world.getBlockState(blockPos2).equals(getFullyGrownLeek())) {
                    if (this.world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                        this.world.levelEvent(2001, blockPos2, Block.getId(ModBlocks.LEEK_CROP.get().defaultBlockState())); // 2001 is block break particles
                        this.world.setBlock(blockPos2, Blocks.AIR.defaultBlockState(), 3);
                        this.miku.setEatingLeek(false);
                        this.miku.heal(4);
                    }
                    this.miku.onEatingGrass();
                }
            }
        }
    }

    public BlockPos locateFullyGrownLeek(BlockPos mobPos) {
        List<BlockPos> POSITION_CHECKS = List.of(
                mobPos.east(),
                mobPos.west(),
                mobPos.north(),
                mobPos.south(),
                mobPos.above(),
                mobPos.above().east(),
                mobPos.above().west(),
                mobPos.above().north(),
                mobPos.above().south()
        );
        for (BlockPos testPos : POSITION_CHECKS) {
            if (this.world.getBlockState(testPos).equals(getFullyGrownLeek())) {
                return testPos;
            }
        }
        return mobPos;
    }
}