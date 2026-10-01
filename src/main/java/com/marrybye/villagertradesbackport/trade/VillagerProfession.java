package com.marrybye.villagertradesbackport.trade;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.marrybye.villagertradesbackport.compat.ModCompatItems;

public enum VillagerProfession {

    ARMORER("Armorer", 3, 1),
    BUTCHER("Butcher", 4, 1),
    CARTOGRAPHER("Cartographer", 1, 2),
    CLERIC("Cleric", 2, 1),
    FARMER("Farmer", 0, 1),
    FISHERMAN("Fisherman", 0, 2),
    FLETCHER("Fletcher", 0, 4),
    LEATHERWORKER("Leatherworker", 4, 2),
    LIBRARIAN("Librarian", 1, 1),
    MASON("Mason", 3, 4),
    SHEPHERD("Shepherd", 0, 3),
    TOOLSMITH("Toolsmith", 3, 3),
    WEAPONSMITH("Weaponsmith", 3, 2),
    NITWIT("Nitwit", 5, 1);

    private final String title;
    private final int vanillaProfession;
    private final int careerId;

    VillagerProfession(String title, int vanillaProfession, int careerId) {
        this.title = title;
        this.vanillaProfession = vanillaProfession;
        this.careerId = careerId;
    }

    public String getTitle() {
        return this.title;
    }

    public int getVanillaProfession() {
        return this.vanillaProfession;
    }

    public int getCareerId() {
        return this.careerId;
    }

    public boolean isJobSiteBlock(Block block) {
        if (block == null) return false;
        switch (this) {
            case ARMORER:
                return block == ModCompatItems.getBlock("etfuturum", "blast_furnace", Blocks.furnace)
                    || block == ModCompatItems.getBlock("etfuturum", "lit_blast_furnace", Blocks.lit_furnace);
            case BUTCHER:
                return block == ModCompatItems.getBlock("etfuturum", "smoker", Blocks.furnace)
                    || block == ModCompatItems.getBlock("etfuturum", "lit_smoker", Blocks.lit_furnace);
            case CARTOGRAPHER:
                return block == ModCompatItems.getBlock("etfuturum", "cartography_table", Blocks.crafting_table);
            case CLERIC:
                return block == Blocks.brewing_stand;
            case FARMER:
                return block == ModCompatItems.getBlock("etfuturum", "composter", Blocks.hay_block);
            case FISHERMAN:
                return block == ModCompatItems.getBlock("etfuturum", "barrel", Blocks.chest);
            case FLETCHER:
                return block == ModCompatItems.getBlock("etfuturum", "fletching_table", Blocks.crafting_table);
            case LEATHERWORKER:
                return block == Blocks.cauldron;
            case LIBRARIAN:
                return block == ModCompatItems.getBlock("etfuturum", "lectern", Blocks.bookshelf);
            case MASON:
                return block == ModCompatItems.getBlock("etfuturum", "stonecutter", Blocks.stonebrick);
            case SHEPHERD:
                return block == ModCompatItems.getBlock("etfuturum", "loom", Blocks.wool);
            case TOOLSMITH:
                return block == ModCompatItems.getBlock("etfuturum", "smithing_table", Blocks.anvil);
            case WEAPONSMITH:
                return block == ModCompatItems.getBlock("etfuturum", "grindstone", Blocks.anvil);
            case NITWIT:
            default:
                return false;
        }
    }

    public static VillagerProfession getProfessionFromBlock(Block block) {
        if (block == null) return null;
        for (VillagerProfession prof : values()) {
            if (prof != NITWIT && prof.isJobSiteBlock(block)) {
                return prof;
            }
        }
        return null;
    }

    public static VillagerProfession fromIds(int vanillaProf, int careerId) {
        for (VillagerProfession p : values()) {
            if (p.vanillaProfession == vanillaProf && p.careerId == careerId) {
                return p;
            }
        }
        // Fallback matching by profession
        switch (vanillaProf) {
            case 0:
                return FARMER;
            case 1:
                return LIBRARIAN;
            case 2:
                return CLERIC;
            case 3:
                return ARMORER;
            case 4:
                return BUTCHER;
            case 5:
                return NITWIT;
            default:
                return null;
        }
    }
}
