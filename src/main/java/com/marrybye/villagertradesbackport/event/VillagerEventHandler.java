package com.marrybye.villagertradesbackport.event;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.marrybye.villagertradesbackport.compat.VillageNamesCompat;
import com.marrybye.villagertradesbackport.trade.VillagerProfession;
import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class VillagerEventHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityVillager) {
            VillagerTradeManager.updateVillagerAI((EntityVillager) event.entityLiving);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.world.isRemote && event.entity instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) event.entity;
            if (VillagerTradeManager.isCustomizableVillager(villager)) {
                VillagerProfession prof = VillagerTradeManager.getProfession(villager);
                if (prof == null) {
                    VillagerTradeManager.setProfession(villager, null);
                } else {
                    VillageNamesCompat.sendModernSkinUpdate(villager);
                }
            }
        }
    }
}
