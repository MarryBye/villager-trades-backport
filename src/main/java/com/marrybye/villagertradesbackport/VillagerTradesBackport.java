package com.marrybye.villagertradesbackport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = VillagerTradesBackport.MODID,
    version = Tags.VERSION,
    name = "Villager Trades Backport",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:UniMixins;required-after:gtnhlib;required-after:etfuturum;after:VillageNames")
public class VillagerTradesBackport {

    public static final String MODID = "villagertradesbackport";
    public static final Logger LOG = LogManager.getLogger(MODID);

    @Mod.Instance(MODID)
    public static VillagerTradesBackport instance;

    @SidedProxy(
        clientSide = "com.marrybye.villagertradesbackport.ClientProxy",
        serverSide = "com.marrybye.villagertradesbackport.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }
}
