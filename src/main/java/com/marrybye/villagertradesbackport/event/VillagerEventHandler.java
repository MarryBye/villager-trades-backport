package com.marrybye.villagertradesbackport.event;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.event.entity.living.LivingEvent;

import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class VillagerEventHandler {

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityVillager) {
            VillagerTradeManager.updateVillagerAI((EntityVillager) event.entityLiving);
        }
    }
}
