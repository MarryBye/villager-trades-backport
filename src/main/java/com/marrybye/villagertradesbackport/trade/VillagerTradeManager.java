package com.marrybye.villagertradesbackport.trade;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

    public static final int UNEMPLOYED_PROFESSION_ID = 6;

    /**
     * Checks if a villager can have our customized leveling and trading system.
     * Modded villagers (TiC, Thaumcraft, etc.) with profession >= 5 (unless VillageNames Nitwit or Unemployed) return
     * false.
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
        if (prof == UNEMPLOYED_PROFESSION_ID) {
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

        if (villager.getProfession() == 5) {
            setProfession(villager, VillagerProfession.NITWIT);
            return VillagerProfession.NITWIT;
        }

        return null;
    }

    public static void setProfession(EntityVillager villager, VillagerProfession prof) {
        if (prof == null) {
            villager.getEntityData()
                .removeTag("VTB_ProfessionName");
            villager.setProfession(UNEMPLOYED_PROFESSION_ID);
            ((AccessorEntityVillager) villager).setBuyingList(new MerchantRecipeList());
            if (IS_VILLAGE_NAMES_LOADED) {
                try {
                    astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager
                        .get(villager);
                    if (ev != null) {
                        ev.setCareer(0);
                        ev.setProfessionLevel(0);
                        VillageNamesCompat.sendModernSkinUpdate(villager);
                    }
                } catch (Throwable ignored) {}
            }
            return;
        }

        villager.getEntityData()
            .setString("VTB_ProfessionName", prof.name());
        villager.setProfession(prof.getVanillaProfession());

        if (IS_VILLAGE_NAMES_LOADED) {
            try {
                astrotibs.villagenames.ieep.ExtendedVillager ev = astrotibs.villagenames.ieep.ExtendedVillager
                    .get(villager);
                if (ev != null) {
                    ev.setCareer(prof.getCareerId());
                    ev.setProfessionLevel(Math.max(1, getLevelFromXp(getVillagerXp(villager))));
                    VillageNamesCompat.sendModernSkinUpdate(villager);
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
        List<MerchantRecipe> tier1Trades = ProfessionTrades.generateTradesForTier(prof, 1, villager.getRNG(), 2, list);
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

        // Unlock trades for each newly reached tier without duplicates
        for (int lvl = fromLevel + 1; lvl <= toLevel; lvl++) {
            List<MerchantRecipe> newTrades = ProfessionTrades
                .generateTradesForTier(prof, lvl, villager.getRNG(), 2, buyList);
            for (MerchantRecipe r : newTrades) {
                buyList.add(r);
            }
        }

        VillageNamesCompat.syncVillagerLevel(villager, toLevel);
        VillageNamesCompat.sendModernSkinUpdate(villager);

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

    public static boolean isRecipeDuplicate(MerchantRecipe existing, MerchantRecipe candidate) {
        if (existing == null || candidate == null) return false;

        ItemStack existingSell = existing.getItemToSell();
        ItemStack candidateSell = candidate.getItemToSell();
        ItemStack existingBuy1 = existing.getItemToBuy();
        ItemStack candidateBuy1 = candidate.getItemToBuy();
        ItemStack existingBuy2 = existing.getSecondItemToBuy();
        ItemStack candidateBuy2 = candidate.getSecondItemToBuy();

        if (existingSell == null || candidateSell == null || existingBuy1 == null || candidateBuy1 == null) {
            return false;
        }

        // 1. Exact match on buy and sell stacks
        if (ItemStack.areItemStacksEqual(existingSell, candidateSell)
            && ItemStack.areItemStacksEqual(existingBuy1, candidateBuy1)
            && areItemStacksEqualOrNull(existingBuy2, candidateBuy2)) {
            return true;
        }

        // 2. Both are sell-trades (player pays emeralds/items to get a product)
        if (existingSell.getItem() == candidateSell.getItem()) {
            if (existingSell.getItem() == Items.enchanted_book) {
                if (areEnchantedBooksEquivalent(existingSell, candidateSell)) {
                    return true;
                }
            } else if (existingSell.getItem() == Items.map) {
                String n1 = existingSell.getDisplayName();
                String n2 = candidateSell.getDisplayName();
                if (n1 != null && n1.equals(n2)) {
                    return true;
                }
            } else if (existingSell.isItemStackDamageable()) {
                // If both are unenchanted: selling the same gear item is duplicate
                if (!existingSell.isItemEnchanted() && !candidateSell.isItemEnchanted()) {
                    return true;
                }
                // If both are enchanted: compare enchantment lists
                if (existingSell.isItemEnchanted() && candidateSell.isItemEnchanted()) {
                    if (areEnchantmentTagsEqual(existingSell, candidateSell)) {
                        return true;
                    }
                }
            } else {
                if (existingSell.getItemDamage() == candidateSell.getItemDamage()) {
                    return true;
                }
            }
        }

        // 3. Both are buy-trades (villager buys resources for emeralds)
        if (existingSell.getItem() == Items.emerald && candidateSell.getItem() == Items.emerald) {
            if (existingBuy1.getItem() == candidateBuy1.getItem()
                && existingBuy1.getItemDamage() == candidateBuy1.getItemDamage()) {
                return true;
            }
        }

        return false;
    }

    private static boolean areItemStacksEqualOrNull(ItemStack a, ItemStack b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return ItemStack.areItemStacksEqual(a, b);
    }

    public static boolean areEnchantedBooksEquivalent(ItemStack b1, ItemStack b2) {
        if (b1 == null || b2 == null) return false;
        NBTTagCompound tag1 = b1.getTagCompound();
        NBTTagCompound tag2 = b2.getTagCompound();
        if (tag1 == null || tag2 == null) return tag1 == tag2;
        NBTTagList list1 = tag1.getTagList("StoredEnchantments", 10);
        NBTTagList list2 = tag2.getTagList("StoredEnchantments", 10);
        if (list1.tagCount() == 0 || list2.tagCount() == 0) return false;

        NBTTagCompound ench1 = list1.getCompoundTagAt(0);
        NBTTagCompound ench2 = list2.getCompoundTagAt(0);
        return ench1.getShort("id") == ench2.getShort("id");
    }

    public static boolean areEnchantmentTagsEqual(ItemStack i1, ItemStack i2) {
        if (i1 == null || i2 == null) return false;
        NBTTagCompound tag1 = i1.getTagCompound();
        NBTTagCompound tag2 = i2.getTagCompound();
        if (tag1 == null || tag2 == null) return tag1 == tag2;
        NBTTagList list1 = tag1.getTagList("ench", 10);
        NBTTagList list2 = tag2.getTagList("ench", 10);
        return list1.equals(list2);
    }

    public static boolean isRecipeDuplicateOfAny(List<?> list, MerchantRecipe candidate) {
        if (list == null || candidate == null) return false;
        for (Object obj : list) {
            if (obj instanceof MerchantRecipe) {
                if (isRecipeDuplicate((MerchantRecipe) obj, candidate)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Called periodically during EntityVillager.onLivingUpdate() to handle workstation checking,
     * profession claiming for 0 XP villagers, workstation break loss, and twice-a-day restocking.
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

        // Tick workstation & profession logic every 20 ticks (1 second)
        if (villager.ticksExisted % 20 != 0) {
            return;
        }

        NBTTagCompound tag = villager.getEntityData();
        int xp = getVillagerXp(villager);
        VillagerProfession prof = getProfession(villager);

        // 1. Check existing claimed workstation
        if (tag.getBoolean("VTB_HasJobSite")) {
            int jx = tag.getInteger("VTB_JobSiteX");
            int jy = tag.getInteger("VTB_JobSiteY");
            int jz = tag.getInteger("VTB_JobSiteZ");

            double distSq = villager.getDistanceSq(jx + 0.5D, jy + 0.5D, jz + 0.5D);
            if (distSq <= 256.0D) { // Within 16 blocks
                Block currentBlock = villager.worldObj.getBlock(jx, jy, jz);
                if (prof == null || !prof.isJobSiteBlock(currentBlock)) {
                    // Workstation is gone / broken!
                    tag.setBoolean("VTB_HasJobSite", false);
                    tag.removeTag("VTB_JobSiteX");
                    tag.removeTag("VTB_JobSiteY");
                    tag.removeTag("VTB_JobSiteZ");

                    if (xp == 0) {
                        // Unlocked: villager loses profession!
                        setProfession(villager, null);
                        ((AccessorEntityVillager) villager).setBuyingList(new MerchantRecipeList());
                        villager.worldObj.setEntityState(villager, (byte) 13);
                        villager.worldObj.playSoundAtEntity(villager, "mob.villager.no", 1.0F, 1.0F);
                        return;
                    }
                }
            }
        }

        // 2. If no claimed workstation, try to claim one nearby
        if (!tag.getBoolean("VTB_HasJobSite")) {
            if (xp == 0 && prof == null) {
                // Unemployed: find any unclaimed workstation within 4 blocks
                int[] coords = findUnclaimedWorkstation(villager, null);
                if (coords != null) {
                    Block block = villager.worldObj.getBlock(coords[0], coords[1], coords[2]);
                    VillagerProfession newProf = VillagerProfession.getProfessionFromBlock(block);
                    if (newProf != null && newProf != VillagerProfession.NITWIT) {
                        tag.setBoolean("VTB_HasJobSite", true);
                        tag.setInteger("VTB_JobSiteX", coords[0]);
                        tag.setInteger("VTB_JobSiteY", coords[1]);
                        tag.setInteger("VTB_JobSiteZ", coords[2]);

                        setProfession(villager, newProf);
                        initVillagerTrades(villager);
                        villager.worldObj.setEntityState(villager, (byte) 14);
                        villager.worldObj.playSoundAtEntity(villager, "mob.villager.yes", 1.0F, 1.0F);
                        return;
                    }
                }
            } else if (prof != null && prof != VillagerProfession.NITWIT) {
                // Locked profession: look for a matching workstation
                int[] coords = findUnclaimedWorkstation(villager, prof);
                if (coords != null) {
                    tag.setBoolean("VTB_HasJobSite", true);
                    tag.setInteger("VTB_JobSiteX", coords[0]);
                    tag.setInteger("VTB_JobSiteY", coords[1]);
                    tag.setInteger("VTB_JobSiteZ", coords[2]);
                    villager.worldObj.setEntityState(villager, (byte) 14);
                    villager.worldObj.playSoundAtEntity(villager, "mob.villager.yes", 1.0F, 1.0F);
                }
            }
        }

        // 3. Restocking logic: up to 2 times a day when near their claimed workstation
        int restocksToday = tag.getInteger("VTB_RestocksToday");
        if (restocksToday < 2 && prof != null
            && prof != VillagerProfession.NITWIT
            && tag.getBoolean("VTB_HasJobSite")) {
            // Work hours: morning to evening (2000 to 10000)
            if (dayTime >= 2000L && dayTime <= 10000L) {
                int jx = tag.getInteger("VTB_JobSiteX");
                int jy = tag.getInteger("VTB_JobSiteY");
                int jz = tag.getInteger("VTB_JobSiteZ");
                if (villager.getDistanceSq(jx + 0.5D, jy + 0.5D, jz + 0.5D) <= 16.0D) {
                    AccessorEntityVillager acc = (AccessorEntityVillager) villager;
                    MerchantRecipeList buyList = acc.getBuyingList();
                    if (buyList != null && needsRestock(buyList)) {
                        restockAllTrades(buyList);
                        tag.setInteger("VTB_RestocksToday", restocksToday + 1);
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

    private static int[] findUnclaimedWorkstation(EntityVillager villager, VillagerProfession requiredProf) {
        int x = MathHelper.floor_double(villager.posX);
        int y = MathHelper.floor_double(villager.posY);
        int z = MathHelper.floor_double(villager.posZ);

        for (int dy = -2; dy <= 2; dy++) {
            for (int dx = -4; dx <= 4; dx++) {
                for (int dz = -4; dz <= 4; dz++) {
                    int bx = x + dx;
                    int by = y + dy;
                    int bz = z + dz;
                    Block block = villager.worldObj.getBlock(bx, by, bz);
                    if (block == null || block == net.minecraft.init.Blocks.air) continue;

                    if (requiredProf != null) {
                        if (!requiredProf.isJobSiteBlock(block)) continue;
                    } else {
                        if (VillagerProfession.getProfessionFromBlock(block) == null) continue;
                    }

                    if (!isJobSiteClaimedByAnother(villager.worldObj, bx, by, bz, villager)) {
                        return new int[] { bx, by, bz };
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJobSiteClaimedByAnother(net.minecraft.world.World world, int x, int y, int z,
        EntityVillager self) {
        @SuppressWarnings("unchecked")
        List<EntityVillager> villagers = world.getEntitiesWithinAABB(
            EntityVillager.class,
            net.minecraft.util.AxisAlignedBB.getBoundingBox(x - 16, y - 8, z - 16, x + 16, y + 8, z + 16));
        for (EntityVillager v : villagers) {
            if (v != self && v.isEntityAlive()) {
                NBTTagCompound tag = v.getEntityData();
                if (tag.getBoolean("VTB_HasJobSite")) {
                    if (tag.getInteger("VTB_JobSiteX") == x && tag.getInteger("VTB_JobSiteY") == y
                        && tag.getInteger("VTB_JobSiteZ") == z) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
