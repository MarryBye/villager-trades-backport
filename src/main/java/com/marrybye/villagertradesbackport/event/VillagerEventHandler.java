package com.marrybye.villagertradesbackport.event;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

import com.marrybye.villagertradesbackport.compat.VillageNamesCompat;
import com.marrybye.villagertradesbackport.mixins.AccessorEntityVillager;
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
                    ((AccessorEntityVillager) villager).setBuyingList(new MerchantRecipeList());
                } else {
                    VillageNamesCompat.sendModernSkinUpdate(villager);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.target instanceof EntityVillager) {
            EntityVillager villager = (EntityVillager) event.target;
            if (VillagerTradeManager.isCustomizableVillager(villager)) {
                VillagerProfession prof = VillagerTradeManager.getProfession(villager);
                if (prof == null || prof == VillagerProfession.NITWIT) {
                    ((AccessorEntityVillager) villager).setBuyingList(new MerchantRecipeList());
                    villager.worldObj.playSoundAtEntity(villager, "mob.villager.no", 1.0F, 1.0F);
                    event.setCanceled(true);
                }
            }
        }
    }
}
