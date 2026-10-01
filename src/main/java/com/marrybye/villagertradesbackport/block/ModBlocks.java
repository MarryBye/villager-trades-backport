package com.marrybye.villagertradesbackport.block;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModBlocks {

    public static BlockLectern lectern;
    public static BlockGrindstone grindstone;

    public static void init() {
        lectern = new BlockLectern();
        GameRegistry.registerBlock(lectern, "lectern");
        GameRegistry.registerTileEntity(TileEntityLectern.class, "vtb_lectern");

        grindstone = new BlockGrindstone();
        GameRegistry.registerBlock(grindstone, "grindstone");

        // Crafting recipes (authentic Mojang recipes)
        GameRegistry.addRecipe(
            new ShapedOreRecipe(new ItemStack(lectern), "SSS", " B ", " S ", 'S', "slabWood", 'B', Blocks.bookshelf));

        GameRegistry.addRecipe(
            new ShapedOreRecipe(
                new ItemStack(grindstone),
                "I-I",
                "# #",
                'I',
                Items.stick,
                '-',
                new ItemStack(Blocks.stone_slab, 1, 0),
                '#',
                "plankWood"));
    }
}
