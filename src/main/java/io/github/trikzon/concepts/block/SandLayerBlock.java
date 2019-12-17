package io.github.trikzon.concepts.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldView;

import java.util.Random;

public class SandLayerBlock extends SnowBlock {

    public SandLayerBlock() {
        super(Block.Settings.copy(Blocks.SAND));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos.down());
        Block block = blockState.getBlock();
        return Block.isFaceFullSquare(blockState.getCollisionShape(world, pos.down()), Direction.UP) || (block instanceof SnowBlock && blockState.get(LAYERS) == 8);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // To stop it from melting
    }
}
