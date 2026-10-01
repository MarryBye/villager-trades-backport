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
        if (block == null || block == Blocks.air) return false;
        boolean etLoaded = cpw.mods.fml.common.Loader.isModLoaded("etfuturum");

        switch (this) {
            case ARMORER:
                if (etLoaded) {
                    Block blast = ModCompatItems.getBlock("etfuturum", "blast_furnace", null);
                    Block litBlast = ModCompatItems.getBlock("etfuturum", "lit_blast_furnace", null);
                    return (blast != null && block == blast) || (litBlast != null && block == litBlast);
                }
                return block == Blocks.furnace || block == Blocks.lit_furnace;
            case BUTCHER:
                if (etLoaded) {
                    Block smoker = ModCompatItems.getBlock("etfuturum", "smoker", null);
                    Block litSmoker = ModCompatItems.getBlock("etfuturum", "lit_smoker", null);
                    return (smoker != null && block == smoker) || (litSmoker != null && block == litSmoker);
                }
                return false;
            case CARTOGRAPHER:
                if (etLoaded) {
                    Block cart = ModCompatItems.getBlock("etfuturum", "cartography_table", null);
                    return cart != null && block == cart;
                }
                return false;
            case CLERIC:
                return block == Blocks.brewing_stand;
            case FARMER:
                if (etLoaded) {
                    Block composter = ModCompatItems.getBlock("etfuturum", "composter", null);
                    return composter != null && block == composter;
                }
                return block == Blocks.hay_block;
            case FISHERMAN:
                if (etLoaded) {
                    Block barrel = ModCompatItems.getBlock("etfuturum", "barrel", null);
                    return barrel != null && block == barrel;
                }
                return false;
            case FLETCHER:
                if (etLoaded) {
                    Block fletcher = ModCompatItems.getBlock("etfuturum", "fletching_table", null);
                    return fletcher != null && block == fletcher;
                }
                return false;
            case LEATHERWORKER:
                return block == Blocks.cauldron;
            case LIBRARIAN:
                Block lectern = ModCompatItems.getBlock("etfuturum", "lectern", null);
                if (lectern != null && block == lectern) return true;
                return block == Blocks.bookshelf;
            case MASON:
                if (etLoaded) {
                    Block stonecutter = ModCompatItems.getBlock("etfuturum", "stonecutter", null);
                    return stonecutter != null && block == stonecutter;
                }
                return false;
            case SHEPHERD:
                if (etLoaded) {
                    Block loom = ModCompatItems.getBlock("etfuturum", "loom", null);
                    return loom != null && block == loom;
                }
                return false;
            case TOOLSMITH:
                if (etLoaded) {
                    Block smith = ModCompatItems.getBlock("etfuturum", "smithing_table", null);
                    return smith != null && block == smith;
                }
                return false;
            case WEAPONSMITH:
                Block grindstone = ModCompatItems.getBlock("etfuturum", "grindstone", null);
                if (grindstone != null && block == grindstone) return true;
                return block == Blocks.anvil;
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
