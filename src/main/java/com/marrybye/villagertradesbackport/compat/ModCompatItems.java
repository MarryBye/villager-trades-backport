package com.marrybye.villagertradesbackport.compat;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModCompatItems {

    public static Item getItem(String modId, String name, Item fallback) {
        Item item = GameRegistry.findItem(modId, name);
        return item != null ? item : fallback;
    }

    public static Block getBlock(String modId, String name, Block fallback) {
        Block block = GameRegistry.findBlock(modId, name);
        return block != null ? block : fallback;
    }

    public static ItemStack getItemStack(String modId, String name, int count, int meta, ItemStack fallback) {
        Item item = GameRegistry.findItem(modId, name);
        if (item != null) {
            return new ItemStack(item, count, meta);
        }
        Block block = GameRegistry.findBlock(modId, name);
        if (block != null) {
            return new ItemStack(block, count, meta);
        }
        return fallback != null ? fallback.copy() : null;
    }

    public static ItemStack getSweetBerries(int count) {
        return getItemStack("etfuturum", "sweet_berries", count, 0, new ItemStack(Items.apple, count));
    }

    public static ItemStack getSuspiciousStew(int count) {
        return getItemStack("etfuturum", "suspicious_stew", count, 0, new ItemStack(Items.mushroom_stew, count));
    }

    public static ItemStack getRawRabbit(int count) {
        return getItemStack("etfuturum", "rabbit_raw", count, 0, new ItemStack(Items.chicken, count));
    }

    public static ItemStack getCookedRabbit(int count) {
        return getItemStack("etfuturum", "rabbit_cooked", count, 0, new ItemStack(Items.cooked_chicken, count));
    }

    public static ItemStack getRabbitFoot(int count) {
        return getItemStack("etfuturum", "rabbit_foot", count, 0, new ItemStack(Items.spider_eye, count));
    }

    public static ItemStack getRabbitHide(int count) {
        return getItemStack("etfuturum", "rabbit_hide", count, 0, new ItemStack(Items.leather, count));
    }

    public static ItemStack getRabbitStew(int count) {
        return getItemStack("etfuturum", "rabbit_stew", count, 0, new ItemStack(Items.mushroom_stew, count));
    }

    public static ItemStack getRawMutton(int count) {
        return getItemStack("etfuturum", "mutton_raw", count, 0, new ItemStack(Items.beef, count));
    }

    public static ItemStack getCookedMutton(int count) {
        return getItemStack("etfuturum", "mutton_cooked", count, 0, new ItemStack(Items.cooked_beef, count));
    }

    public static ItemStack getLantern(int count) {
        return getItemStack("etfuturum", "lantern", count, 0, new ItemStack(Blocks.torch, count * 4));
    }

    public static ItemStack getCampfire(int count) {
        return getItemStack("etfuturum", "campfire", count, 0, new ItemStack(Blocks.torch, count * 4));
    }

    public static ItemStack getCrossbow(int count) {
        return getItemStack("etfuturum", "crossbow", count, 0, new ItemStack(Items.bow, count));
    }

    public static ItemStack getBell(int count) {
        return getItemStack("etfuturum", "bell", count, 0, new ItemStack(Blocks.gold_block, count));
    }

    public static ItemStack getBanner(int count, int colorMeta) {
        return getItemStack(
            "etfuturum",
            "banner",
            count,
            colorMeta,
            new ItemStack(Blocks.carpet, count * 2, colorMeta));
    }

    public static ItemStack getShield(int count) {
        return getItemStack("etfuturum", "shield", count, 0, new ItemStack(Items.iron_chestplate, count));
    }

    public static ItemStack getGranite(int count) {
        return getItemStack("etfuturum", "granite", count, 0, new ItemStack(Blocks.stone, count));
    }

    public static ItemStack getDiorite(int count) {
        return getItemStack("etfuturum", "diorite", count, 0, new ItemStack(Blocks.stone, count));
    }

    public static ItemStack getAndesite(int count) {
        return getItemStack("etfuturum", "andesite", count, 0, new ItemStack(Blocks.stone, count));
    }

    public static ItemStack getPolishedGranite(int count) {
        return getItemStack("etfuturum", "polished_granite", count, 0, new ItemStack(Blocks.stone, count));
    }

    public static ItemStack getPolishedDiorite(int count) {
        return getItemStack("etfuturum", "polished_diorite", count, 0, new ItemStack(Blocks.stone, count));
    }

    public static ItemStack getPolishedAndesite(int count) {
        return getItemStack("etfuturum", "polished_andesite", count, 0, new ItemStack(Blocks.stone, count));
    }
}
