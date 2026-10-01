package com.marrybye.villagertradesbackport.network;

import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class ModNetwork {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("villagertrades");

    private static int packetId = 0;

    public static void init() {
        INSTANCE.registerMessage(PacketSelectTrade.Handler.class, PacketSelectTrade.class, packetId++, Side.SERVER);
        INSTANCE.registerMessage(PacketSyncTradeUses.Handler.class, PacketSyncTradeUses.class, packetId++, Side.CLIENT);
    }

    public static void sendSelectTrade(int tradeIndex) {
        INSTANCE.sendToServer(new PacketSelectTrade(tradeIndex));
    }

    public static void sendSyncTradeUses(EntityPlayerMP player, int level, int xp, int minXp, int maxXp,
        String professionTitle, int[] uses) {
        INSTANCE.sendTo(new PacketSyncTradeUses(level, xp, minXp, maxXp, professionTitle, uses), player);
    }
}
