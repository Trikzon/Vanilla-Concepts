package io.github.trikzon.concepts.mixin;

import io.github.trikzon.concepts.mixin_interface.EditableMiningTool;
import net.minecraft.block.Block;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends MiningToolItem implements EditableMiningTool {

    @Shadow @Final private static Set<Block> EFFECTIVE_BLOCKS;

    public ShovelItemMixin(float attackDamage, float attackSpeed, ToolMaterial material, Set<Block> effectiveBlocks, Settings settings) {
        super(attackDamage, attackSpeed, material, effectiveBlocks, settings);
    }

    @Override
    public void trikzon_addEffectiveBlock(Block block) {
        EFFECTIVE_BLOCKS.add(block);
    }
}
