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
