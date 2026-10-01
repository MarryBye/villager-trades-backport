package com.marrybye.villagertradesbackport.trade;

import static com.marrybye.villagertradesbackport.trade.TradeOfferFactory.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

import com.marrybye.villagertradesbackport.compat.ModCompatItems;
import com.marrybye.villagertradesbackport.trade.TradeOfferFactory.ITradeGenerator;

public class ProfessionTrades {

    private static final Map<VillagerProfession, Map<Integer, List<ITradeGenerator>>> POOLS = new EnumMap<>(
        VillagerProfession.class);

    static {
        for (VillagerProfession prof : VillagerProfession.values()) {
            POOLS.put(prof, new HashMap<>());
            for (int lvl = 1; lvl <= 5; lvl++) {
                POOLS.get(prof)
                    .put(lvl, new ArrayList<>());
            }
        }
        initTrades();
    }

    private static void add(VillagerProfession prof, int level, ITradeGenerator gen) {
        POOLS.get(prof)
            .get(level)
            .add(gen);
    }

    private static void initTrades() {
        // --- ARMORER ---
        // Novice
        add(
            VillagerProfession.ARMORER,
            1,
            create(new ItemStack(Items.coal, 15), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.ARMORER,
            1,
            create(new ItemStack(Items.emerald, 5), new ItemStack(Items.iron_helmet), 12, 1, 0.2F));
        add(
            VillagerProfession.ARMORER,
            1,
            create(new ItemStack(Items.emerald, 9), new ItemStack(Items.iron_chestplate), 12, 1, 0.2F));
        add(
            VillagerProfession.ARMORER,
            1,
            create(new ItemStack(Items.emerald, 7), new ItemStack(Items.iron_leggings), 12, 1, 0.2F));
        add(
            VillagerProfession.ARMORER,
            1,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.iron_boots), 12, 1, 0.2F));
        // Apprentice
        add(
            VillagerProfession.ARMORER,
            2,
            create(new ItemStack(Items.iron_ingot, 4), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.ARMORER,
            2,
            create(new ItemStack(Items.emerald, 36), ModCompatItems.getBell(1), 12, 5, 0.2F));
        add(
            VillagerProfession.ARMORER,
            2,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.chainmail_leggings), 12, 5, 0.2F));
        add(
            VillagerProfession.ARMORER,
            2,
            create(new ItemStack(Items.emerald, 1), new ItemStack(Items.chainmail_boots), 12, 5, 0.2F));
        // Journeyman
        add(
            VillagerProfession.ARMORER,
            3,
            create(new ItemStack(Items.lava_bucket), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.ARMORER,
            3,
            create(new ItemStack(Items.diamond, 1), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.ARMORER,
            3,
            create(new ItemStack(Items.emerald, 1), new ItemStack(Items.chainmail_helmet), 12, 10, 0.2F));
        add(
            VillagerProfession.ARMORER,
            3,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.chainmail_chestplate), 12, 10, 0.2F));
        add(
            VillagerProfession.ARMORER,
            3,
            create(new ItemStack(Items.emerald, 5), ModCompatItems.getShield(1), 12, 10, 0.2F));
        // Expert
        add(VillagerProfession.ARMORER, 4, enchantedGear(19, 33, Items.diamond_leggings, 5, 19, 3, 15, 0.2F));
        add(VillagerProfession.ARMORER, 4, enchantedGear(13, 27, Items.diamond_boots, 5, 19, 3, 15, 0.2F));
        // Master
        add(VillagerProfession.ARMORER, 5, enchantedGear(13, 27, Items.diamond_helmet, 20, 39, 3, 30, 0.2F));
        add(VillagerProfession.ARMORER, 5, enchantedGear(21, 35, Items.diamond_chestplate, 20, 39, 3, 30, 0.2F));

        // --- BUTCHER ---
        // Novice
        add(
            VillagerProfession.BUTCHER,
            1,
            create(new ItemStack(Items.chicken, 14), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            1,
            create(ModCompatItems.getRawRabbit(4), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            1,
            create(new ItemStack(Items.porkchop, 7), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            1,
            create(new ItemStack(Items.emerald), ModCompatItems.getRabbitStew(1), 12, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.BUTCHER,
            2,
            create(new ItemStack(Items.coal, 15), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Items.cooked_chicken, 8), 16, 5, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Items.cooked_porkchop, 5), 16, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.BUTCHER,
            3,
            create(new ItemStack(Items.beef, 10), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.BUTCHER,
            3,
            create(ModCompatItems.getRawMutton(7), new ItemStack(Items.emerald), 16, 20, 0.05F));
        // Expert
        add(
            VillagerProfession.BUTCHER,
            4,
            create(new ItemStack(Items.coal, 10), new ItemStack(Items.emerald), 12, 30, 0.05F));
        // Master
        add(
            VillagerProfession.BUTCHER,
            5,
            create(ModCompatItems.getSweetBerries(10), new ItemStack(Items.emerald), 12, 30, 0.05F));

        // --- CARTOGRAPHER ---
        // Novice
        add(
            VillagerProfession.CARTOGRAPHER,
            1,
            create(new ItemStack(Items.paper, 24), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.CARTOGRAPHER,
            1,
            create(new ItemStack(Items.emerald, 7), new ItemStack(Items.map), 12, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.CARTOGRAPHER,
            2,
            create(new ItemStack(Blocks.glass_pane, 11), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.CARTOGRAPHER,
            2,
            create(
                new ItemStack(Items.emerald, 8),
                new ItemStack(Items.compass),
                new ItemStack(Items.map),
                12,
                5,
                0.2F));
        // Journeyman
        add(
            VillagerProfession.CARTOGRAPHER,
            3,
            create(new ItemStack(Items.compass), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.CARTOGRAPHER,
            3,
            create(
                new ItemStack(Items.emerald, 13),
                new ItemStack(Items.compass),
                new ItemStack(Items.map),
                12,
                10,
                0.2F));
        // Expert
        add(
            VillagerProfession.CARTOGRAPHER,
            4,
            create(new ItemStack(Items.emerald, 7), new ItemStack(Items.item_frame), 12, 15, 0.05F));
        add(
            VillagerProfession.CARTOGRAPHER,
            4,
            create(new ItemStack(Items.emerald, 3), ModCompatItems.getBanner(1, 0), 12, 15, 0.05F));
        // Master
        add(
            VillagerProfession.CARTOGRAPHER,
            5,
            create(new ItemStack(Items.emerald, 8), new ItemStack(Items.painting, 3), 12, 30, 0.05F));
        add(
            VillagerProfession.CARTOGRAPHER,
            5,
            create(
                new ItemStack(Items.emerald, 14),
                new ItemStack(Items.compass),
                new ItemStack(Items.map),
                12,
                30,
                0.2F));

        // --- CLERIC ---
        // Novice
        add(
            VillagerProfession.CLERIC,
            1,
            create(new ItemStack(Items.rotten_flesh, 32), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.CLERIC,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.redstone, 2), 12, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.CLERIC,
            2,
            create(new ItemStack(Items.gold_ingot, 3), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.CLERIC,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Items.dye, 1, 4), 12, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.CLERIC,
            3,
            create(ModCompatItems.getRabbitFoot(2), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.CLERIC,
            3,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Blocks.glowstone), 12, 10, 0.05F));
        // Expert
        add(
            VillagerProfession.CLERIC,
            4,
            create(new ItemStack(Items.glass_bottle, 9), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.CLERIC,
            4,
            create(new ItemStack(Items.emerald, 5), new ItemStack(Items.ender_pearl), 12, 15, 0.05F));
        // Master
        add(
            VillagerProfession.CLERIC,
            5,
            create(new ItemStack(Items.nether_wart, 22), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.CLERIC,
            5,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.experience_bottle), 12, 30, 0.05F));

        // --- FARMER ---
        // Novice
        add(
            VillagerProfession.FARMER,
            1,
            create(new ItemStack(Items.wheat, 20), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FARMER,
            1,
            create(new ItemStack(Items.potato, 26), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FARMER,
            1,
            create(new ItemStack(Items.carrot, 22), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FARMER,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.bread, 6), 16, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.FARMER,
            2,
            create(new ItemStack(Blocks.pumpkin, 6), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.FARMER,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Items.pumpkin_pie, 4), 12, 5, 0.05F));
        add(
            VillagerProfession.FARMER,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Items.apple, 4), 16, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.FARMER,
            3,
            create(new ItemStack(Blocks.melon_block, 4), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.FARMER,
            3,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.cookie, 18), 12, 10, 0.05F));
        // Expert
        add(
            VillagerProfession.FARMER,
            4,
            create(new ItemStack(Items.emerald), ModCompatItems.getSuspiciousStew(1), 12, 15, 0.05F));
        add(
            VillagerProfession.FARMER,
            4,
            create(new ItemStack(Items.emerald), new ItemStack(Items.cake), 12, 15, 0.05F));
        // Master
        add(
            VillagerProfession.FARMER,
            5,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.golden_carrot, 3), 12, 30, 0.05F));
        add(
            VillagerProfession.FARMER,
            5,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.speckled_melon, 3), 12, 30, 0.05F));

        // --- FISHERMAN ---
        // Novice
        add(
            VillagerProfession.FISHERMAN,
            1,
            create(new ItemStack(Items.string, 20), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            1,
            create(new ItemStack(Items.coal, 10), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            1,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.fish, 1, 0), 16, 1, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            1,
            create(
                new ItemStack(Items.fish, 6, 0),
                new ItemStack(Items.emerald),
                new ItemStack(Items.cooked_fished, 6, 0),
                16,
                1,
                0.05F));
        // Apprentice
        add(
            VillagerProfession.FISHERMAN,
            2,
            create(new ItemStack(Items.fish, 15, 0), new ItemStack(Items.emerald), 16, 10, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            2,
            create(new ItemStack(Items.emerald, 2), ModCompatItems.getCampfire(1), 12, 5, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            2,
            create(
                new ItemStack(Items.fish, 6, 1),
                new ItemStack(Items.emerald),
                new ItemStack(Items.cooked_fished, 6, 1),
                16,
                5,
                0.05F));
        // Journeyman
        add(
            VillagerProfession.FISHERMAN,
            3,
            create(new ItemStack(Items.fish, 13, 1), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(VillagerProfession.FISHERMAN, 3, enchantedGear(8, 22, Items.fishing_rod, 5, 19, 3, 10, 0.2F));
        // Expert
        add(
            VillagerProfession.FISHERMAN,
            4,
            create(new ItemStack(Items.fish, 6, 2), new ItemStack(Items.emerald), 12, 30, 0.05F));
        // Master
        add(
            VillagerProfession.FISHERMAN,
            5,
            create(new ItemStack(Items.fish, 4, 3), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.FISHERMAN,
            5,
            create(new ItemStack(Items.boat), new ItemStack(Items.emerald), 12, 30, 0.05F));

        // --- FLETCHER ---
        // Novice
        add(
            VillagerProfession.FLETCHER,
            1,
            create(new ItemStack(Items.stick, 32), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.FLETCHER,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.arrow, 16), 12, 1, 0.05F));
        add(
            VillagerProfession.FLETCHER,
            1,
            create(
                new ItemStack(Blocks.gravel, 10),
                new ItemStack(Items.emerald),
                new ItemStack(Items.flint, 10),
                12,
                1,
                0.05F));
        // Apprentice
        add(
            VillagerProfession.FLETCHER,
            2,
            create(new ItemStack(Items.flint, 26), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.FLETCHER,
            2,
            create(new ItemStack(Items.emerald, 2), new ItemStack(Items.bow), 12, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.FLETCHER,
            3,
            create(new ItemStack(Items.string, 14), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.FLETCHER,
            3,
            create(new ItemStack(Items.emerald, 3), ModCompatItems.getCrossbow(1), 12, 10, 0.05F));
        // Expert
        add(
            VillagerProfession.FLETCHER,
            4,
            create(new ItemStack(Items.feather, 24), new ItemStack(Items.emerald), 16, 30, 0.05F));
        add(VillagerProfession.FLETCHER, 4, enchantedGear(7, 21, Items.bow, 5, 19, 3, 15, 0.2F));
        // Master
        add(
            VillagerProfession.FLETCHER,
            5,
            create(new ItemStack(Blocks.tripwire_hook, 8), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(VillagerProfession.FLETCHER, 5, enchantedGear(8, 22, Items.bow, 20, 39, 3, 15, 0.2F));
        add(
            VillagerProfession.FLETCHER,
            5,
            create(
                new ItemStack(Items.emerald, 2),
                new ItemStack(Items.arrow, 5),
                new ItemStack(Items.arrow, 5),
                12,
                30,
                0.05F));

        // --- LEATHERWORKER ---
        // Novice
        add(
            VillagerProfession.LEATHERWORKER,
            1,
            create(new ItemStack(Items.leather, 6), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.LEATHERWORKER,
            1,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.leather_leggings), 12, 1, 0.2F));
        add(
            VillagerProfession.LEATHERWORKER,
            1,
            create(new ItemStack(Items.emerald, 7), new ItemStack(Items.leather_chestplate), 12, 1, 0.2F));
        // Apprentice
        add(
            VillagerProfession.LEATHERWORKER,
            2,
            create(new ItemStack(Items.flint, 26), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.LEATHERWORKER,
            2,
            create(new ItemStack(Items.emerald, 5), new ItemStack(Items.leather_helmet), 12, 5, 0.2F));
        add(
            VillagerProfession.LEATHERWORKER,
            2,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.leather_boots), 12, 5, 0.2F));
        // Journeyman
        add(
            VillagerProfession.LEATHERWORKER,
            3,
            create(ModCompatItems.getRabbitHide(9), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.LEATHERWORKER,
            3,
            create(new ItemStack(Items.emerald, 7), new ItemStack(Items.leather_chestplate), 12, 10, 0.2F));
        // Expert
        add(
            VillagerProfession.LEATHERWORKER,
            4,
            create(new ItemStack(Items.leather, 4), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.LEATHERWORKER,
            4,
            create(new ItemStack(Items.emerald, 6), new ItemStack(Items.saddle), 12, 15, 0.2F));
        // Master
        add(
            VillagerProfession.LEATHERWORKER,
            5,
            create(new ItemStack(Items.emerald, 5), new ItemStack(Items.leather_helmet), 12, 30, 0.2F));
        add(
            VillagerProfession.LEATHERWORKER,
            5,
            create(new ItemStack(Items.emerald, 6), new ItemStack(Items.saddle), 12, 30, 0.2F));

        // --- LIBRARIAN ---
        // Novice
        add(
            VillagerProfession.LIBRARIAN,
            1,
            create(new ItemStack(Items.paper, 24), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.LIBRARIAN,
            1,
            create(new ItemStack(Items.emerald, 9), new ItemStack(Blocks.bookshelf), 12, 1, 0.05F));
        add(VillagerProfession.LIBRARIAN, 1, enchantedBook(12, 1, 0.2F));
        // Apprentice
        add(
            VillagerProfession.LIBRARIAN,
            2,
            create(new ItemStack(Items.book, 4), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.LIBRARIAN,
            2,
            create(new ItemStack(Items.emerald), ModCompatItems.getLantern(1), 12, 5, 0.05F));
        add(VillagerProfession.LIBRARIAN, 2, enchantedBook(12, 5, 0.2F));
        // Journeyman
        add(
            VillagerProfession.LIBRARIAN,
            3,
            create(new ItemStack(Items.dye, 5, 0), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(
            VillagerProfession.LIBRARIAN,
            3,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.glass, 4), 12, 10, 0.05F));
        add(VillagerProfession.LIBRARIAN, 3, enchantedBook(12, 10, 0.2F));
        // Expert
        add(
            VillagerProfession.LIBRARIAN,
            4,
            create(new ItemStack(Items.writable_book, 2), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.LIBRARIAN,
            4,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.compass), 12, 15, 0.05F));
        add(
            VillagerProfession.LIBRARIAN,
            4,
            create(new ItemStack(Items.emerald, 5), new ItemStack(Items.clock), 12, 15, 0.05F));
        add(VillagerProfession.LIBRARIAN, 4, enchantedBook(12, 15, 0.2F));
        // Master
        add(
            VillagerProfession.LIBRARIAN,
            5,
            create(new ItemStack(Items.emerald, 20), new ItemStack(Items.name_tag), 12, 30, 0.05F));

        // --- MASON ---
        // Novice
        add(
            VillagerProfession.MASON,
            1,
            create(new ItemStack(Items.clay_ball, 10), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.MASON,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.brick_block, 10), 16, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.MASON,
            2,
            create(new ItemStack(Blocks.stone, 20), new ItemStack(Items.emerald), 16, 10, 0.05F));
        add(
            VillagerProfession.MASON,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.stonebrick, 4, 3), 16, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.MASON,
            3,
            create(ModCompatItems.getGranite(16), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.MASON,
            3,
            create(ModCompatItems.getAndesite(16), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.MASON,
            3,
            create(ModCompatItems.getDiorite(16), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.MASON,
            3,
            create(new ItemStack(Items.emerald), ModCompatItems.getPolishedGranite(4), 16, 10, 0.05F));
        add(
            VillagerProfession.MASON,
            3,
            create(new ItemStack(Items.emerald), ModCompatItems.getPolishedAndesite(4), 16, 10, 0.05F));
        add(
            VillagerProfession.MASON,
            3,
            create(new ItemStack(Items.emerald), ModCompatItems.getPolishedDiorite(4), 16, 10, 0.05F));
        // Expert
        add(
            VillagerProfession.MASON,
            4,
            create(new ItemStack(Items.quartz, 12), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(
            VillagerProfession.MASON,
            4,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.stained_hardened_clay, 1, 0), 12, 15, 0.05F));
        // Master
        add(
            VillagerProfession.MASON,
            5,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.quartz_block, 1, 2), 12, 30, 0.05F));
        add(
            VillagerProfession.MASON,
            5,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.quartz_block, 1, 0), 12, 30, 0.05F));

        // --- SHEPHERD ---
        // Novice
        add(
            VillagerProfession.SHEPHERD,
            1,
            create(new ItemStack(Blocks.wool, 18, 0), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            1,
            create(new ItemStack(Blocks.wool, 18, 12), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            1,
            create(new ItemStack(Blocks.wool, 18, 15), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            1,
            create(new ItemStack(Blocks.wool, 18, 7), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            1,
            create(new ItemStack(Items.emerald, 2), new ItemStack(Items.shears), 12, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.SHEPHERD,
            2,
            create(new ItemStack(Items.dye, 12, 15), new ItemStack(Items.emerald), 16, 10, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            2,
            create(new ItemStack(Items.dye, 12, 8), new ItemStack(Items.emerald), 16, 10, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            2,
            create(new ItemStack(Items.dye, 12, 0), new ItemStack(Items.emerald), 16, 10, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.wool, 1, 14), 16, 5, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            2,
            create(new ItemStack(Items.emerald), new ItemStack(Blocks.carpet, 4, 14), 16, 5, 0.05F));
        // Journeyman
        add(
            VillagerProfession.SHEPHERD,
            3,
            create(new ItemStack(Items.dye, 12, 11), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            3,
            create(new ItemStack(Items.dye, 12, 14), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            3,
            create(new ItemStack(Items.dye, 12, 1), new ItemStack(Items.emerald), 16, 20, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            3,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.bed), 12, 10, 0.05F));
        // Expert
        add(
            VillagerProfession.SHEPHERD,
            4,
            create(new ItemStack(Items.dye, 12, 4), new ItemStack(Items.emerald), 16, 30, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            4,
            create(new ItemStack(Items.dye, 12, 2), new ItemStack(Items.emerald), 16, 30, 0.05F));
        add(
            VillagerProfession.SHEPHERD,
            4,
            create(new ItemStack(Items.emerald, 3), ModCompatItems.getBanner(1, 14), 12, 15, 0.05F));
        // Master
        add(
            VillagerProfession.SHEPHERD,
            5,
            create(new ItemStack(Items.emerald, 2), new ItemStack(Items.painting, 3), 12, 30, 0.05F));

        // --- TOOLSMITH ---
        // Novice
        add(
            VillagerProfession.TOOLSMITH,
            1,
            create(new ItemStack(Items.coal, 15), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.TOOLSMITH,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.stone_axe), 12, 1, 0.2F));
        add(
            VillagerProfession.TOOLSMITH,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.stone_shovel), 12, 1, 0.2F));
        add(
            VillagerProfession.TOOLSMITH,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.stone_pickaxe), 12, 1, 0.2F));
        add(
            VillagerProfession.TOOLSMITH,
            1,
            create(new ItemStack(Items.emerald), new ItemStack(Items.stone_hoe), 12, 1, 0.2F));
        // Apprentice
        add(
            VillagerProfession.TOOLSMITH,
            2,
            create(new ItemStack(Items.iron_ingot, 4), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.TOOLSMITH,
            2,
            create(new ItemStack(Items.emerald, 36), ModCompatItems.getBell(1), 12, 5, 0.2F));
        // Journeyman
        add(
            VillagerProfession.TOOLSMITH,
            3,
            create(new ItemStack(Items.flint, 30), new ItemStack(Items.emerald), 12, 20, 0.05F));
        add(VillagerProfession.TOOLSMITH, 3, enchantedGear(6, 20, Items.iron_axe, 5, 19, 3, 10, 0.2F));
        add(VillagerProfession.TOOLSMITH, 3, enchantedGear(7, 21, Items.iron_shovel, 5, 19, 3, 10, 0.2F));
        add(VillagerProfession.TOOLSMITH, 3, enchantedGear(8, 22, Items.iron_pickaxe, 5, 19, 3, 10, 0.2F));
        add(
            VillagerProfession.TOOLSMITH,
            3,
            create(new ItemStack(Items.emerald, 4), new ItemStack(Items.diamond_hoe), 3, 10, 0.2F));
        // Expert
        add(
            VillagerProfession.TOOLSMITH,
            4,
            create(new ItemStack(Items.diamond), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(VillagerProfession.TOOLSMITH, 4, enchantedGear(17, 31, Items.diamond_axe, 5, 19, 3, 15, 0.2F));
        add(VillagerProfession.TOOLSMITH, 4, enchantedGear(10, 24, Items.diamond_shovel, 5, 19, 3, 15, 0.2F));
        // Master
        add(VillagerProfession.TOOLSMITH, 5, enchantedGear(18, 32, Items.diamond_pickaxe, 20, 39, 3, 30, 0.2F));

        // --- WEAPONSMITH ---
        // Novice
        add(
            VillagerProfession.WEAPONSMITH,
            1,
            create(new ItemStack(Items.coal, 15), new ItemStack(Items.emerald), 16, 2, 0.05F));
        add(
            VillagerProfession.WEAPONSMITH,
            1,
            create(new ItemStack(Items.emerald, 3), new ItemStack(Items.iron_axe), 12, 1, 0.2F));
        add(VillagerProfession.WEAPONSMITH, 1, enchantedGear(7, 21, Items.iron_sword, 5, 19, 3, 1, 0.05F));
        // Apprentice
        add(
            VillagerProfession.WEAPONSMITH,
            2,
            create(new ItemStack(Items.iron_ingot, 4), new ItemStack(Items.emerald), 12, 10, 0.05F));
        add(
            VillagerProfession.WEAPONSMITH,
            2,
            create(new ItemStack(Items.emerald, 36), ModCompatItems.getBell(1), 12, 5, 0.2F));
        // Journeyman
        add(
            VillagerProfession.WEAPONSMITH,
            3,
            create(new ItemStack(Items.flint, 24), new ItemStack(Items.emerald), 12, 20, 0.05F));
        // Expert
        add(
            VillagerProfession.WEAPONSMITH,
            4,
            create(new ItemStack(Items.diamond), new ItemStack(Items.emerald), 12, 30, 0.05F));
        add(VillagerProfession.WEAPONSMITH, 4, enchantedGear(17, 31, Items.diamond_axe, 5, 19, 3, 15, 0.2F));
        // Master
        add(VillagerProfession.WEAPONSMITH, 5, enchantedGear(13, 27, Items.diamond_sword, 20, 39, 3, 30, 0.2F));
    }

    public static List<MerchantRecipe> generateTradesForTier(VillagerProfession prof, int level, Random random,
        int count) {
        if (prof == null || prof == VillagerProfession.NITWIT) {
            return Collections.emptyList();
        }
        Map<Integer, List<ITradeGenerator>> levelMap = POOLS.get(prof);
        if (levelMap == null) return Collections.emptyList();
        List<ITradeGenerator> pool = levelMap.get(level);
        if (pool == null || pool.isEmpty()) return Collections.emptyList();

        List<ITradeGenerator> shuffled = new ArrayList<>(pool);
        Collections.shuffle(shuffled, random);

        int toSelect = Math.min(count, shuffled.size());
        List<MerchantRecipe> result = new ArrayList<>(toSelect);
        for (int i = 0; i < toSelect; i++) {
            result.add(
                shuffled.get(i)
                    .generate(random));
        }
        return result;
    }
}
