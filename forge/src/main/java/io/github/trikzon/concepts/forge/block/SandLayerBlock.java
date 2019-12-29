/* ===========================================================================
 * Copyright 2019 Trikzon
 *
 * Concepts is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * File: SandLayerBlock.java
 * Date: 2019-12-29
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.concepts.forge.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class SandLayerBlock extends FallingBlock {

    private static final IntegerProperty LAYERS = BlockStateProperties.LAYERS_1_8;

    private static final VoxelShape[] LAYERS_TO_SHAPE;

    static {
        LAYERS_TO_SHAPE = new VoxelShape[]{
                VoxelShapes.empty(),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
                Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
        };
    }

    public SandLayerBlock() {
        super(Block.Properties.from(Blocks.SAND));
        this.setDefaultState(this.getDefaultState().with(LAYERS, 1));
    }

    @Override
    public void onEndFalling(World world, BlockPos pos, BlockState fallingBlockState, BlockState currentStateInPos) {
        if (currentStateInPos.getBlock() == this) {
            int fallingLayerAmt = fallingBlockState.get(LAYERS);
            int currentLayerAmt = currentStateInPos.get(LAYERS);

            int amtToFill = 8 - currentLayerAmt;

            if (fallingLayerAmt > amtToFill) {
                fallingLayerAmt -= amtToFill;
                world.setBlockState(pos, currentStateInPos.with(LAYERS, currentLayerAmt + amtToFill));
            }

            if (world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
                world.setBlockState(pos.up(), fallingBlockState.with(LAYERS, fallingLayerAmt));
            }
        }
    }

    /**
     * Fabric: FallingBlock#scheduledTick
     */
    @Override
    public void func_225534_a_(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (canFallThrough(world.getBlockState(pos.down())) && pos.getY() >= 0) {
            FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, world.getBlockState(pos));
            this.onStartFalling(fallingBlockEntity);
            world.addEntity(fallingBlockEntity);
        }
    }

    /**
     * Fabric: FallingBlock#configureFallingBlockEntity
     */
    @Override
    protected void onStartFalling(FallingBlockEntity fallingEntity) {

    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return LAYERS_TO_SHAPE[state.get(LAYERS)];
    }

    /**
     * Fabric: Block#hasSidedTransparency
     */
    @Override
    public boolean func_220074_n(BlockState state) {
        return true;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext ctx) {
        int i = state.get(LAYERS);
        if (ctx.getItem().getItem() == this.asItem() && i < 8) {
            if (ctx.replacingClickedOnBlock()) {
                return ctx.getFace() == Direction.UP;
            } else {
                return true;
            }
        } else {
            return i == 1;
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getPos());
        if (blockState.getBlock() == this) {
            int i = blockState.get(LAYERS);
            return blockState.with(LAYERS, Math.min(8, i + 1));
        } else {
            return super.getStateForPlacement(ctx);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LAYERS);
    }
}
