package com.marrybye.villagertradesbackport.trade;

import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

import com.marrybye.villagertradesbackport.mixins.AccessorMerchantRecipe;

public class TradeOfferFactory {

    public interface ITradeGenerator {

        MerchantRecipe generate(Random random);
    }

    public static ITradeGenerator create(ItemStack buy1, ItemStack sell, int maxUses, int xp, float mult) {
        return random -> buildRecipe(buy1.copy(), null, sell.copy(), maxUses, xp, mult);
    }

    public static ITradeGenerator create(ItemStack buy1, ItemStack buy2, ItemStack sell, int maxUses, int xp,
        float mult) {
        return random -> buildRecipe(buy1.copy(), buy2 != null ? buy2.copy() : null, sell.copy(), maxUses, xp, mult);
    }

    public static ITradeGenerator randomCost(Item buyItem, int minBuy, int maxBuy, ItemStack sell, int maxUses, int xp,
        float mult) {
        return random -> {
            int count = minBuy + (maxBuy > minBuy ? random.nextInt(maxBuy - minBuy + 1) : 0);
            return buildRecipe(new ItemStack(buyItem, count), null, sell.copy(), maxUses, xp, mult);
        };
    }

    public static ITradeGenerator randomEmeraldSell(int minEmeralds, int maxEmeralds, ItemStack sellStack, int maxUses,
        int xp, float mult) {
        return random -> {
            int cost = minEmeralds + (maxEmeralds > minEmeralds ? random.nextInt(maxEmeralds - minEmeralds + 1) : 0);
            return buildRecipe(new ItemStack(Items.emerald, cost), null, sellStack.copy(), maxUses, xp, mult);
        };
    }

    public static ITradeGenerator enchantedGear(int minEmeralds, int maxEmeralds, Item gearItem, int minEnchantLvl,
        int maxEnchantLvl, int maxUses, int xp, float mult) {
        return random -> {
            int cost = minEmeralds + (maxEmeralds > minEmeralds ? random.nextInt(maxEmeralds - minEmeralds + 1) : 0);
            int enchantLvl = minEnchantLvl
                + (maxEnchantLvl > minEnchantLvl ? random.nextInt(maxEnchantLvl - minEnchantLvl + 1) : 0);
            ItemStack gear = new ItemStack(gearItem);
            EnchantmentHelper.addRandomEnchantment(random, gear, enchantLvl);
            return buildRecipe(new ItemStack(Items.emerald, cost), null, gear, maxUses, xp, mult);
        };
    }

    public static ITradeGenerator enchantedBook(int maxUses, int xp, float mult) {
        return random -> {
            // Pick a valid enchantment from Enchantment.enchantmentsList
            Enchantment[] validEnchantments = getValidEnchantments();
            if (validEnchantments.length == 0) {
                return buildRecipe(
                    new ItemStack(Items.emerald, 10),
                    new ItemStack(Items.book),
                    new ItemStack(Items.book),
                    maxUses,
                    xp,
                    mult);
            }
            Enchantment ench = validEnchantments[random.nextInt(validEnchantments.length)];
            int level = ench.getMinLevel()
                + (ench.getMaxLevel() > ench.getMinLevel() ? random.nextInt(ench.getMaxLevel() - ench.getMinLevel() + 1)
                    : 0);
            int baseCost = 5 + (level * 3) + random.nextInt(15);
            if (baseCost > 64) baseCost = 64;
            if (baseCost < 5) baseCost = 5;

            ItemStack book = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(ench, level));
            return buildRecipe(
                new ItemStack(Items.emerald, baseCost),
                new ItemStack(Items.book, 1),
                book,
                maxUses,
                xp,
                mult);
        };
    }

    private static Enchantment[] validEnchantsCache = null;

    private static Enchantment[] getValidEnchantments() {
        if (validEnchantsCache != null) return validEnchantsCache;
        java.util.List<Enchantment> list = new java.util.ArrayList<>();
        for (Enchantment e : Enchantment.enchantmentsList) {
            if (e != null && e.getName() != null) {
                list.add(e);
            }
        }
        validEnchantsCache = list.toArray(new Enchantment[0]);
        return validEnchantsCache;
    }

    public static MerchantRecipe buildRecipe(ItemStack buy1, ItemStack buy2, ItemStack sell, int maxUses, int xp,
        float mult) {
        MerchantRecipe recipe = new MerchantRecipe(buy1, buy2, sell);
        ((AccessorMerchantRecipe) recipe).setMaxTradeUses(maxUses);
        ((AccessorMerchantRecipe) recipe).setToolUses(0);
        ((ITradeOffer) recipe).setVillagerXp(xp);
        ((ITradeOffer) recipe).setPriceMultiplier(mult);
        return recipe;
    }
}
