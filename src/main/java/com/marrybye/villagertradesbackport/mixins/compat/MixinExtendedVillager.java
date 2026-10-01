package com.marrybye.villagertradesbackport.mixins.compat;

import net.minecraft.entity.passive.EntityVillager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.marrybye.villagertradesbackport.trade.VillagerProfession;
import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

import astrotibs.villagenames.ieep.ExtendedVillager;

@Mixin(value = ExtendedVillager.class, remap = false)
public abstract class MixinExtendedVillager {

    @Inject(method = "determineProfessionLevel", at = @At("HEAD"), cancellable = true)
    private static void onDetermineProfessionLevel(EntityVillager villager, CallbackInfoReturnable<Integer> cir) {
        if (VillagerTradeManager.isCustomizableVillager(villager)) {
            VillagerProfession prof = VillagerTradeManager.getProfession(villager);
            if (prof == null) {
                cir.setReturnValue(0);
            } else {
                cir.setReturnValue(
                    Math.max(1, VillagerTradeManager.getLevelFromXp(VillagerTradeManager.getVillagerXp(villager))));
            }
        }
    }
}
