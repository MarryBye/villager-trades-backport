package com.marrybye.villagertradesbackport;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        com.marrybye.villagertradesbackport.network.ModNetwork.init();

        VillagerTradesBackport.LOG.info(Config.greeting);
        VillagerTradesBackport.LOG.info("I am Villager Trades Backport at version " + Tags.VERSION);
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        net.minecraftforge.common.MinecraftForge.EVENT_BUS
            .register(new com.marrybye.villagertradesbackport.event.VillagerEventHandler());
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        com.marrybye.villagertradesbackport.compat.VillageNamesCompat.init();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
