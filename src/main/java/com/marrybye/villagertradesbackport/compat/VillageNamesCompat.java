package com.marrybye.villagertradesbackport.compat;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.Loader;

public class VillageNamesCompat {

    private static final boolean IS_VILLAGE_NAMES_LOADED = Loader.isModLoaded("VillageNames");

    public static int getVillagerLevel(IMerchant merchant, MerchantRecipeList trades) {
        if (IS_VILLAGE_NAMES_LOADED && merchant instanceof EntityVillager) {
            try {
                int vnLevel = getVillageNamesProfessionLevel((EntityVillager) merchant);
                if (vnLevel >= 1 && vnLevel <= 5) {
                    return vnLevel;
                }
            } catch (Throwable ignored) {}
        }

        // Fallback calculation based on trade count
        if (trades != null && !trades.isEmpty()) {
            int count = trades.size();
            if (count <= 2) return 1;
            if (count <= 4) return 2;
            if (count <= 6) return 3;
            if (count <= 8) return 4;
            return 5;
        }

        return 1;
    }

    private static int getVillageNamesProfessionLevel(EntityVillager villager) {
        astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager.get(villager);
        if (ev != null) {
            int level = ev.getProfessionLevel();
            if (level >= 1 && level <= 5) {
                return level;
            }
        }
        return astrotibs.villagenames.ieep.ExtendedVillager.determineProfessionLevel(villager);
    }
}
