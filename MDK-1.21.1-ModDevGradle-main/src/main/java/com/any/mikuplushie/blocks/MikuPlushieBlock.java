package com.any.mikuplushie.blocks;

import com.any.mikuplushie.entity.AbstractPlushEntity;
import com.any.mikuplushie.registry.ModItems;
import com.any.mikuplushie.registry.ModParticles;
import com.any.mikuplushie.util.ModUtil;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Registry;

public class MikuPlushieBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape SHAPE = Block.box(4.5, 0.0, 4.5, 11.5, 13.5, 11.5);

    public MikuPlushieBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {

        if (stack.is(ModItems.VOCALOID_HEART.get())) {
            if (!level.isClientSide) {

                String blockName = ModUtil.getBlockIdFromBlockPos(level, pos);
                String entityName = ModUtil.getEntityNameFromBlockId(blockName);

                Vec3 entitySpawnLocation = pos.getCenter().subtract(0, 0.5, 0);
                Registry<EntityType<?>> entityTypeRegistry = level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE);

                for (EntityType<?> type : entityTypeRegistry) {
                    if (type.getDescriptionId().contains(entityName)) {

                        AbstractPlushEntity spawned = (AbstractPlushEntity) type.create(level);

                        if (spawned != null) {
                            spawned.setVariantByBlock(blockName);
                            spawned.lookAt(EntityAnchorArgument.Anchor.EYES, player.position().subtract(pos.getCenter()));
                            spawned.tame(player);
                            spawned.setPos(entitySpawnLocation);
                            level.addFreshEntity(spawned);
                        }
                    }
                }

                level.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.BLOCKS, 0.5f, 1);
                level.destroyBlock(pos, false, player);
                return ItemInteractionResult.SUCCESS;
            } else {
                for (int particles = 0; particles < 100; particles++) {
                    level.addParticle(
                            ModParticles.MIKU_SPAWN.get(),
                            pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D,
                            0, 0, 0
                    );
                }
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        String currentPlush = ModUtil.getBlockIdFromBlockState(state);
        ModUtil.playPlushSound(level, pos, currentPlush, "oie");
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        for (DeferredItem<Item> plushItem : ModItems.PLUSH_ITEMS) {
            String plushNames = ModUtil.getBlockIdFromItem(plushItem.get());
            String currentPlush = ModUtil.getBlockIdFromBlockState(state);
            if (plushNames.equals(currentPlush)) {
                return plushItem.get().getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        String currentPlush = ModUtil.getBlockIdFromBlockState(state);
        ModUtil.playPlushSound(level, pos, currentPlush, "bye");
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}