package io.github.trikzon.concepts.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.explosion.Explosion;

import java.util.List;

public class SleepingBagBlock extends HorizontalFacingBlock {

    private static final EnumProperty<BedPart> PART = Properties.BED_PART;

    private static final BooleanProperty OCCUPIED = Properties.OCCUPIED;

    private static final VoxelShape VOXEL_SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);

    private final DyeColor color;

    public SleepingBagBlock(DyeColor color) {
        super(Block.Settings.copy(Blocks.WHITE_BED));
        this.color = color;
        this.setDefaultState(this.getDefaultState().with(PART, BedPart.FOOT).with(OCCUPIED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.CONSUME;

        if (state.get(PART) != BedPart.HEAD) {
            pos = pos.offset(state.get(FACING));
            state = world.getBlockState(pos);

            if (state.getBlock() != this) return ActionResult.CONSUME;
        }

        if (world.dimension.canPlayersSleep() && world.getBiome(pos) != Biomes.NETHER) {
            if (state.get(OCCUPIED)) {
                if (!this.isVillagerSleeping(world, pos)) {
                    player.addChatMessage(new TranslatableText("block.minecraft.bed.occupied"), true);
                }

                return ActionResult.SUCCESS;
            } else {
                player.trySleep(pos).ifLeft((sleepFailureReason -> {
                    if (sleepFailureReason != null) {
                        player.addChatMessage(sleepFailureReason.toText(), true);
                    }
                }));
                return ActionResult.SUCCESS;
            }
        } else {
            world.removeBlock(pos, false);
            BlockPos blockPos = pos.offset(state.get(FACING).getOpposite());
            if (world.getBlockState(blockPos).getBlock() == this) world.removeBlock(blockPos, false);

            world.createExplosion(null, DamageSource.netherBed(), (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, true, Explosion.DestructionType.DESTROY);
            return ActionResult.SUCCESS;
        }
    }

    private boolean isVillagerSleeping(World world, BlockPos blockPos) {
        List<VillagerEntity> list = world.getEntities(VillagerEntity.class, new Box(blockPos), LivingEntity::isSleeping);
        if (list.isEmpty()) {
            return false;
        } else{
            list.get(0).wakeUp();
            return true;
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
        if (facing == getDirectionTowardsOtherPart(state.get(PART), state.get(FACING)))
            return neighborState.getBlock() == this && neighborState.get(PART) != state.get(PART) ? state.with(OCCUPIED, neighborState.get(OCCUPIED)) : Blocks.AIR.getDefaultState();
        else return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
    }

    private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
        return part == BedPart.FOOT ? direction : direction.getOpposite();
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BedPart bedPart = state.get(PART);
        BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, state.get(FACING)));
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getBlock() == this && blockState.get(PART) != bedPart) {
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
            world.playLevelEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
            if (!world.isClient && !player.isCreative()) {
                ItemStack itemStack = player.getMainHandStack();
                dropStacks(state, world, pos, null, player, itemStack);
                dropStacks(blockState, world, blockPos, null, player, itemStack);
            }

            player.incrementStat(Stats.MINED.getOrCreateStat(this));
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing();
        BlockPos blockPos = ctx.getBlockPos();
        BlockPos blockPos1 = blockPos.offset(direction);
        return ctx.getWorld().getBlockState(blockPos1).canReplace(ctx) ? this.getDefaultState().with(FACING, direction) : null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (!world.isClient) {
            BlockPos blockPos = pos.offset(state.get(FACING));
            world.setBlockState(blockPos, state.with(PART, BedPart.HEAD), 3);
            world.updateNeighbors(pos, Blocks.AIR);
            state.updateNeighborStates(world, pos, 3);
        }
    }

    @Environment(EnvType.CLIENT)
    public static Direction getDirection(BlockView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.getBlock() instanceof SleepingBagBlock ? blockState.get(FACING) : null;
    }

    @Override
    public BlockSoundGroup getSoundGroup(BlockState state) {
        return BlockSoundGroup.WOOL;
    }

    @Override
    public MaterialColor getMapColor(BlockState state, BlockView view, BlockPos pos) {
        return state.get(PART) == BedPart.FOOT ? this.color.getMaterialColor() : MaterialColor.WEB;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext ePos) {
        return VOXEL_SHAPE;
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.DESTROY;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, OCCUPIED);
    }
}