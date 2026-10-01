package com.marrybye.villagertradesbackport.compat;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import cpw.mods.fml.common.Loader;

public class VillageNamesCompat {

    private static final boolean IS_VILLAGE_NAMES_LOADED = Loader.isModLoaded("VillageNames");

    public static void init() {
        if (IS_VILLAGE_NAMES_LOADED) {
            try {
                astrotibs.villagenames.config.GeneralConfig.modernVillagerTrades = false;
                registerUnemployedProfession();
            } catch (Throwable ignored) {}
        }
    }

    @SuppressWarnings("unchecked")
    public static void registerUnemployedProfession() {
        if (!IS_VILLAGE_NAMES_LOADED) return;
        try {
            int unempId = com.marrybye.villagertradesbackport.trade.VillagerTradeManager.UNEMPLOYED_PROFESSION_ID;
            if (!astrotibs.villagenames.config.GeneralConfig.professionID_a.contains(unempId)) {
                astrotibs.villagenames.config.GeneralConfig.professionID_a.add(unempId);
                astrotibs.villagenames.config.GeneralConfig.careerAsset_a.add("");
            }
        } catch (Throwable ignored) {}
    }

    public static int getVillagerLevel(IMerchant merchant, MerchantRecipeList trades) {
        if (merchant instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) merchant;
            if (villager.getEntityData()
                .hasKey("VTB_Level")) {
                int saved = villager.getEntityData()
                    .getInteger("VTB_Level");
                if (saved >= 1 && saved <= 5) {
                    return saved;
                }
            }
            if (com.marrybye.villagertradesbackport.trade.VillagerTradeManager.isCustomizableVillager(villager)) {
                return com.marrybye.villagertradesbackport.trade.VillagerTradeManager.getLevelFromXp(
                    com.marrybye.villagertradesbackport.trade.VillagerTradeManager.getVillagerXp(villager));
            }
            if (IS_VILLAGE_NAMES_LOADED) {
                try {
                    int vnLevel = getVillageNamesProfessionLevel(villager);
                    if (vnLevel >= 1 && vnLevel <= 5) {
                        return vnLevel;
                    }
                } catch (Throwable ignored) {}
            }
        }

        // Fallback calculation for initial level
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

        if (merchant instanceof EntityVillager) {
            syncVillagerLevel((EntityVillager) merchant, level);
        }

        return level;
    }

    public static void syncVillagerLevel(EntityVillager villager, int level) {
        villager.getEntityData()
            .setInteger("VTB_Level", level);
        if (IS_VILLAGE_NAMES_LOADED) {
            try {
                syncVillageNamesProfessionLevel(villager, level);
            } catch (Throwable ignored) {}
        }
    }

    public static void sendModernSkinUpdate(EntityVillager villager) {
        if (!IS_VILLAGE_NAMES_LOADED || villager.worldObj == null || villager.worldObj.isRemote) {
            return;
        }
        try {
            registerUnemployedProfession();
            astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager
                .get(villager);
            if (ev == null) return;

            if (ev.getBiomeType() == -1) {
                ev.setBiomeType(astrotibs.villagenames.utility.FunctionsVN.returnBiomeTypeForEntityLocation(villager));
            }
            if (ev.getSkinTone() == -99) {
                ev.setSkinTone(astrotibs.villagenames.utility.FunctionsVN.returnSkinToneForEntityLocation(villager));
            }

            cpw.mods.fml.common.network.NetworkRegistry.TargetPoint targetPoint = new cpw.mods.fml.common.network.NetworkRegistry.TargetPoint(
                villager.dimension,
                villager.posX,
                villager.posY,
                villager.posZ,
                80.0D);

            astrotibs.villagenames.VillageNames.VNNetworkWrapper.sendToAllAround(
                new astrotibs.villagenames.network.MessageModernVillagerSkin(
                    villager.getEntityId(),
                    villager.getProfession(),
                    ev.getCareer(),
                    ev.getBiomeType(),
                    ev.getProfessionLevel(),
                    ev.getSkinTone()),
                targetPoint);
        } catch (Throwable ignored) {}
    }

    private static int getVillageNamesProfessionLevel(EntityVillager villager) {
        astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager.get(villager);
        return ev != null ? ev.getProfessionLevel() : -1;
    }

    private static void syncVillageNamesProfessionLevel(EntityVillager villager, int level) {
        astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager.get(villager);
        if (ev != null && ev.getProfessionLevel() != level) {
            ev.setProfessionLevel(level);
            sendModernSkinUpdate(villager);
        }
    }
}
