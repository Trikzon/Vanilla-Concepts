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
 * File: ShovelItemMixin.java
 * Date: 2019-12-29
 * Revision:
 * Author: Trikzon
 * =========================================================================== */
package io.github.trikzon.concepts.fabric.mixin.common;

import io.github.trikzon.concepts.fabric.mixin.extension.EditableMiningTool;
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
