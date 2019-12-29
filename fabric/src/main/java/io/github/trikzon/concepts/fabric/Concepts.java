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
 * File: Concepts.java
 * Date: 2019-12-29
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.concepts.fabric;

import io.github.trikzon.concepts.fabric.block.SandLayerBlock;
import io.github.trikzon.concepts.fabric.block.SleepingBagBlock;
import io.github.trikzon.phantomlib.fabric.registry.RegUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class Concepts implements ModInitializer {

    public static final String MOD_ID = "concepts";

    @Override
    public void onInitialize() {
        UseBlockCallback.EVENT.register(this::convertToPath);

        register();
    }

    private void register() {

        for (DyeColor color : DyeColor.values()) {
            RegUtils.createBlock(new Identifier(MOD_ID, color.toString() + "_sleeping_bag"), new SleepingBagBlock(color), new Item.Settings().group(ItemGroup.DECORATIONS));
        }

        RegUtils.createBlock(new Identifier(MOD_ID, "sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));
        RegUtils.createBlock(new Identifier(MOD_ID, "red_sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));
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
