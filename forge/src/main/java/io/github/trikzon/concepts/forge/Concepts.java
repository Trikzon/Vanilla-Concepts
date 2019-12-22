package io.github.trikzon.concepts.forge;

import io.github.trikzon.concepts.forge.block.SandLayerBlock;
import io.github.trikzon.phantomlib.forge.registry.RegUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(Concepts.MOD_ID)
public class Concepts {

    public static final String MOD_ID = "concepts";

    public Concepts() {
        register();
    }

    private void register() {

        RegUtils.createBlock(new ResourceLocation(MOD_ID, "sand_layer"), new SandLayerBlock(), new Item.Properties().group(ItemGroup.DECORATIONS));
        RegUtils.createBlock(new ResourceLocation(MOD_ID, "red_sand_layer"), new SandLayerBlock(), new Item.Properties().group(ItemGroup.DECORATIONS));
    }
}
