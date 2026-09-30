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
            level = Math.max(1, Math.min(5, (count + 1) / 2));
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
