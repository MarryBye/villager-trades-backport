package com.marrybye.villagertradesbackport.compat;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.Loader;

public class VillageNamesCompat {

    private static final boolean IS_VILLAGE_NAMES_LOADED = Loader.isModLoaded("VillageNames");

    public static int getVillagerLevel(IMerchant merchant, MerchantRecipeList trades) {
        int level = 1;

        if (trades != null && !trades.isEmpty()) {
            int count = trades.size();
            if (count <= 2) {
                level = 1;
            } else if (count == 3) {
                level = 2;
            } else if (count == 4) {
                level = 3;
            } else if (count == 5) {
                level = 4;
            } else {
                level = 5;
            }
        }

        // Keep VillageNames' profession level (which drives skin badge render) synchronized with actual trade level
        if (IS_VILLAGE_NAMES_LOADED && merchant instanceof EntityVillager) {
            try {
                syncVillageNamesProfessionLevel((EntityVillager) merchant, level);
            } catch (Throwable ignored) {}
        }

        return level;
    }

    private static void syncVillageNamesProfessionLevel(EntityVillager villager, int level) {
        astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager.get(villager);
        if (ev != null && ev.getProfessionLevel() != level) {
            ev.setProfessionLevel(level);
        }
    }
}
