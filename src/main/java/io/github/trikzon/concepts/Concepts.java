package io.github.trikzon.concepts;

import io.github.trikzon.concepts.block.SandLayerBlock;
import io.github.trikzon.concepts.block.SleepingBagBlock;
import net.fabricmc.api.ModInitializer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Concepts implements ModInitializer {

    public static final String MOD_ID = "concepts";

    @Override
    public void onInitialize() {
        for (DyeColor color : DyeColor.values()) {
            createBlockWithItem(new Identifier(MOD_ID, color.toString() + "_sleeping_bag"), new SleepingBagBlock(color), new Item.Settings().group(ItemGroup.DECORATIONS));
        }

        createBlockWithItem(new Identifier(MOD_ID, "sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));
        createBlockWithItem(new Identifier(MOD_ID, "red_sand_layer"), new SandLayerBlock(), new Item.Settings().group(ItemGroup.DECORATIONS));
    }

    public void createBlockWithItem(Identifier id, Block block, Item.Settings settings) {
        Registry.register(Registry.BLOCK, id, block);
        Registry.register(Registry.ITEM, id, new BlockItem(block, settings));
    }
}
