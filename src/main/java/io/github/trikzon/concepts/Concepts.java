package io.github.trikzon.concepts;

import io.github.trikzon.concepts.block.SandLayerBlock;
import io.github.trikzon.concepts.block.SleepingBagBlock;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Concepts implements ModInitializer {

    public static final String MOD_ID = "concepts";

    @Override
    public void onInitialize() {
        for (DyeColor color : DyeColor.values()) {
            createBlockWithItem(new Identifier(MOD_ID, color.toString() + "_sleeping_bag"), new SleepingBagBlock(color), new Item.Settings().group(ItemGroup.DECORATIONS));
        }

        createBlockWithItem(new Identifier(MOD_ID, "sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));
        createBlockWithItem(new Identifier(MOD_ID, "red_sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));

        UseBlockCallback.EVENT.register(this::convertToPath);
    }

    private void createBlockWithItem(Identifier id, Block block, Item.Settings settings) {
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, settings));
    }

    private ActionResult convertToPath(PlayerEntity player, World world, Hand hand, HitResult hitResult) {
//        if (!(player.getStackInHand(hand).getItem() instanceof ShovelItem)) return ActionResult.PASS;
//
//        BlockPos blockPos = new BlockPos(hitResult.getPos());
//        Block block = world.getBlockState(blockPos).getBlock();
//
//        if (block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL) {
//            world.setBlockState(blockPos, Blocks.GRASS_PATH.getDefaultState());
//            return ActionResult.CONSUME;
//        }

        return ActionResult.PASS;
    }
}
