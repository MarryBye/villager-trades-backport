package com.marrybye.villagertradesbackport.mixins.compat;

import net.minecraft.entity.passive.EntityVillager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

import astrotibs.villagenames.utility.FunctionsVN;

@Mixin(value = FunctionsVN.class, remap = false)
public abstract class MixinFunctionsVN {

    @Inject(method = "monitorVillagerTrades", at = @At("HEAD"), cancellable = true)
    private static void onMonitorVillagerTrades(EntityVillager villager, CallbackInfo ci) {
        if (VillagerTradeManager.isCustomizableVillager(villager)) {
            ci.cancel();
        }
    }
}
