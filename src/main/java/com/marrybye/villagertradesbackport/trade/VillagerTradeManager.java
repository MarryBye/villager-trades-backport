package com.marrybye.villagertradesbackport.trade;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import com.marrybye.villagertradesbackport.compat.VillageNamesCompat;
import com.marrybye.villagertradesbackport.mixins.AccessorEntityVillager;
import com.marrybye.villagertradesbackport.mixins.AccessorMerchantRecipe;

import cpw.mods.fml.common.Loader;

public class VillagerTradeManager {

    private static final boolean IS_VILLAGE_NAMES_LOADED = Loader.isModLoaded("VillageNames");

    public static final int XP_LEVEL_1 = 0;
    public static final int XP_LEVEL_2 = 10;
    public static final int XP_LEVEL_3 = 70;
    public static final int XP_LEVEL_4 = 150;
    public static final int XP_LEVEL_5 = 250;

    public static int getMinXpForLevel(int level) {
        switch (level) {
            case 1:
                return XP_LEVEL_1;
            case 2:
                return XP_LEVEL_2;
            case 3:
                return XP_LEVEL_3;
            case 4:
                return XP_LEVEL_4;
            default:
                return XP_LEVEL_5;
        }
    }

    public static int getMaxXpForLevel(int level) {
        switch (level) {
            case 1:
                return XP_LEVEL_2;
            case 2:
                return XP_LEVEL_3;
            case 3:
                return XP_LEVEL_4;
            case 4:
                return XP_LEVEL_5;
            default:
                return XP_LEVEL_5;
        }
    }

    public static int getLevelFromXp(int xp) {
        if (xp < XP_LEVEL_2) return 1;
        if (xp < XP_LEVEL_3) return 2;
        if (xp < XP_LEVEL_4) return 3;
        if (xp < XP_LEVEL_5) return 4;
        return 5;
    }

    /**
     * Checks if this villager is a vanilla / VillageNames villager that should use 1.14+ trades.
     * Modded villagers (TiC, Thaumcraft, etc.) with profession >= 5 (unless VillageNames Nitwit) return false.
     */
    public static boolean isCustomizableVillager(EntityVillager villager) {
        int prof = villager.getProfession();
        if (prof >= 0 && prof <= 4) {
            return true;
        }
        if (prof == 5 && IS_VILLAGE_NAMES_LOADED) {
            // VillageNames Nitwit
            return true;
        }
        return false;
    }

    public static VillagerProfession getProfession(EntityVillager villager) {
        if (!isCustomizableVillager(villager)) {
            return null;
        }

        NBTTagCompound tag = villager.getEntityData();
        if (tag.hasKey("VTB_ProfessionName")) {
            try {
                return VillagerProfession.valueOf(tag.getString("VTB_ProfessionName"));
            } catch (Exception ignored) {}
        }

        int profId = villager.getProfession();
        int careerId = 1;

        if (IS_VILLAGE_NAMES_LOADED) {
            try {
                astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager
                    .get(villager);
                if (ev != null) {
                    careerId = ev.getCareer();
                    if (careerId <= 0) {
                        careerId = astrotibs.villagenames.ieep.ExtendedVillager
                            .pickRandomCareer(villager.getRNG(), profId);
                        ev.setCareer(careerId);
                    }
                }
            } catch (Throwable ignored) {}
        } else {
            // Random career if pure vanilla
            Random rng = villager.getRNG();
            switch (profId) {
                case 0:
                    careerId = 1 + rng.nextInt(4);
                    break; // Farmer, Fisherman, Shepherd, Fletcher
                case 1:
                    careerId = 1 + rng.nextInt(2);
                    break; // Librarian, Cartographer
                case 2:
                    careerId = 1;
                    break; // Cleric
                case 3:
                    careerId = 1 + rng.nextInt(4);
                    break; // Armorer, Weaponsmith, Toolsmith, Mason
                case 4:
                    careerId = 1 + rng.nextInt(2);
                    break; // Butcher, Leatherworker
                case 5:
                    careerId = 1;
                    break; // Nitwit
            }
        }

        VillagerProfession prof = VillagerProfession.fromIds(profId, careerId);
        if (prof != null) {
            setProfession(villager, prof);
        }
        return prof;
    }

    public static void setProfession(EntityVillager villager, VillagerProfession prof) {
        if (prof == null) return;
        villager.getEntityData()
            .setString("VTB_ProfessionName", prof.name());
        villager.setProfession(prof.getVanillaProfession());

        if (IS_VILLAGE_NAMES_LOADED) {
            try {
                astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager
                    .get(villager);
                if (ev != null) {
                    ev.setCareer(prof.getCareerId());
                }
            } catch (Throwable ignored) {}
        }
    }

    public static int getVillagerXp(EntityVillager villager) {
        return villager.getEntityData()
            .getInteger("VTB_Xp");
    }

    public static void setVillagerXp(EntityVillager villager, int xp) {
        villager.getEntityData()
            .setInteger("VTB_Xp", Math.max(0, xp));
        int level = getLevelFromXp(xp);
        villager.getEntityData()
            .setInteger("VTB_Level", level);
        VillageNamesCompat.syncVillagerLevel(villager, level);
    }

    /**
     * Initializes trades for a vanilla/VillageNames villager with 2 trades from Tier 1.
     */
    public static void initVillagerTrades(EntityVillager villager) {
        AccessorEntityVillager acc = (AccessorEntityVillager) villager;
        VillagerProfession prof = getProfession(villager);

        if (prof == null || prof == VillagerProfession.NITWIT) {
            acc.setBuyingList(new MerchantRecipeList());
            return;
        }

        MerchantRecipeList list = new MerchantRecipeList();
        List<MerchantRecipe> tier1Trades = ProfessionTrades.generateTradesForTier(prof, 1, villager.getRNG(), 2);
        for (MerchantRecipe r : tier1Trades) {
            list.add(r);
        }

        acc.setBuyingList(list);
        setVillagerXp(villager, 0);
    }

    /**
     * Called when a trade is used. Increments XP, checks for level up, restocks and unlocks new tier trades.
     */
    public static boolean onTradeUsed(EntityVillager villager, MerchantRecipe recipe, EntityPlayer player) {
        if (!isCustomizableVillager(villager)) {
            return false;
        }

        int currentXp = getVillagerXp(villager);
        int oldLevel = getLevelFromXp(currentXp);

        int tradeXp = (recipe instanceof ITradeOffer) ? ((ITradeOffer) recipe).getVillagerXp() : 1;
        int newXp = currentXp + tradeXp;
        setVillagerXp(villager, newXp);

        int newLevel = getLevelFromXp(newXp);
        boolean leveledUp = (newLevel > oldLevel);

        // Spawn player XP orbs (3-6 XP + 5 bonus on level up)
        if (!villager.worldObj.isRemote && player != null) {
            int playerExp = 3 + villager.getRNG()
                .nextInt(4);
            if (leveledUp) {
                playerExp += 5;
            }
            while (playerExp > 0) {
                int split = EntityXPOrb.getXPSplit(playerExp);
                playerExp -= split;
                villager.worldObj.spawnEntityInWorld(
                    new EntityXPOrb(villager.worldObj, villager.posX, villager.posY + 0.5D, villager.posZ, split));
            }
        }

        if (leveledUp) {
            performLevelUp(villager, oldLevel, newLevel);
        }

        return leveledUp;
    }

    public static void performLevelUp(EntityVillager villager, int fromLevel, int toLevel) {
        AccessorEntityVillager acc = (AccessorEntityVillager) villager;
        VillagerProfession prof = getProfession(villager);
        if (prof == null || prof == VillagerProfession.NITWIT) return;

        MerchantRecipeList buyList = acc.getBuyingList();
        if (buyList == null) {
            buyList = new MerchantRecipeList();
            acc.setBuyingList(buyList);
        }

        // Restock all existing trades immediately upon leveling up
        restockAllTrades(buyList);

        // Unlock 2 trades for each newly reached tier
        for (int lvl = fromLevel + 1; lvl <= toLevel; lvl++) {
            List<MerchantRecipe> newTrades = ProfessionTrades.generateTradesForTier(prof, lvl, villager.getRNG(), 2);
            for (MerchantRecipe r : newTrades) {
                buyList.addToListWithCheck(r);
            }
        }

        // Play level-up visual effects and sound
        villager.worldObj.setEntityState(villager, (byte) 14);
        villager.worldObj.playSoundAtEntity(
            villager,
            "mob.villager.yes",
            1.0F,
            villager.isChild() ? (villager.getRNG()
                .nextFloat()
                - villager.getRNG()
                    .nextFloat())
                * 0.2F + 1.5F
                : (villager.getRNG()
                    .nextFloat()
                    - villager.getRNG()
                        .nextFloat())
                    * 0.2F + 1.0F);
    }

    public static void restockAllTrades(MerchantRecipeList buyList) {
        if (buyList == null) return;
        for (Object obj : (List<?>) buyList) {
            if (obj instanceof MerchantRecipe) {
                MerchantRecipe mr = (MerchantRecipe) obj;
                ((AccessorMerchantRecipe) mr).setToolUses(0);
            }
        }
    }

    /**
     * Called periodically during EntityVillager.onLivingUpdate() to handle workstation checking,
     * profession claiming for 0 XP villagers, and twice-a-day restocking.
     */
    public static void updateVillagerAI(EntityVillager villager) {
        if (villager.worldObj.isRemote || !isCustomizableVillager(villager)) {
            return;
        }

        long worldTime = villager.worldObj.getWorldTime();
        long dayTime = worldTime % 24000L;

        // Reset restock count at start of day (dayTime < 100)
        long currentDay = worldTime / 24000L;
        long lastDay = villager.getEntityData()
            .getLong("VTB_LastDay");
        if (currentDay != lastDay) {
            villager.getEntityData()
                .setLong("VTB_LastDay", currentDay);
            villager.getEntityData()
                .setInteger("VTB_RestocksToday", 0);
        }

        // Only tick workstation logic every 80 ticks (4 seconds)
        if (villager.ticksExisted % 80 != 0) {
            return;
        }

        int xp = getVillagerXp(villager);
        VillagerProfession prof = getProfession(villager);

        // If villager has 0 XP and no locked trades, allow claiming a nearby workstation
        if (xp == 0) {
            Block nearbyWorkstation = findNearbyWorkstation(villager);
            if (nearbyWorkstation != null) {
                VillagerProfession newProf = VillagerProfession.getProfessionFromBlock(nearbyWorkstation);
                if (newProf != null && newProf != prof) {
                    setProfession(villager, newProf);
                    initVillagerTrades(villager);
                    villager.worldObj.setEntityState(villager, (byte) 14);
                    villager.worldObj.playSoundAtEntity(villager, "mob.villager.idle", 1.0F, 1.0F);
                    return;
                }
            }
        }

        // Restocking logic: up to 2 times a day when near their workstation
        int restocksToday = villager.getEntityData()
            .getInteger("VTB_RestocksToday");
        if (restocksToday < 2 && prof != null && prof != VillagerProfession.NITWIT) {
            // Work hours: morning (2000 to 9000)
            if (dayTime >= 2000L && dayTime <= 10000L) {
                if (isNearWorkstation(villager, prof)) {
                    AccessorEntityVillager acc = (AccessorEntityVillager) villager;
                    MerchantRecipeList buyList = acc.getBuyingList();
                    if (buyList != null && needsRestock(buyList)) {
                        restockAllTrades(buyList);
                        villager.getEntityData()
                            .setInteger("VTB_RestocksToday", restocksToday + 1);
                        villager.worldObj.setEntityState(villager, (byte) 14);
                        villager.worldObj.playSoundAtEntity(villager, "mob.villager.yes", 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    private static boolean needsRestock(MerchantRecipeList buyList) {
        for (Object obj : (List<?>) buyList) {
            if (obj instanceof MerchantRecipe) {
                MerchantRecipe mr = (MerchantRecipe) obj;
                if (((AccessorMerchantRecipe) mr).getToolUses() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNearWorkstation(EntityVillager villager, VillagerProfession prof) {
        int x = MathHelper.floor_double(villager.posX);
        int y = MathHelper.floor_double(villager.posY);
        int z = MathHelper.floor_double(villager.posZ);

        for (int dx = -3; dx <= 3; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -3; dz <= 3; dz++) {
                    Block block = villager.worldObj.getBlock(x + dx, y + dy, z + dz);
                    if (prof.isJobSiteBlock(block)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static Block findNearbyWorkstation(EntityVillager villager) {
        int x = MathHelper.floor_double(villager.posX);
        int y = MathHelper.floor_double(villager.posY);
        int z = MathHelper.floor_double(villager.posZ);

        for (int dx = -4; dx <= 4; dx++) {
            for (int dy = -2; dy <= 2; dy++) {
                for (int dz = -4; dz <= 4; dz++) {
                    Block block = villager.worldObj.getBlock(x + dx, y + dy, z + dz);
                    if (VillagerProfession.getProfessionFromBlock(block) != null) {
                        return block;
                    }
                }
            }
        }
        return null;
    }
}
