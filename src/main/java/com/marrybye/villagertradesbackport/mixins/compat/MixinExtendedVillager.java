package com.marrybye.villagertradesbackport.mixins.compat;

import net.minecraft.entity.passive.EntityVillager;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.marrybye.villagertradesbackport.trade.VillagerProfession;
import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

import astrotibs.villagenames.ieep.ExtendedVillager;

@Mixin(value = ExtendedVillager.class, remap = false)
public abstract class MixinExtendedVillager {

    @Shadow
    private EntityVillager villager;

    @Shadow
    private int career;

    @Inject(method = "getProfession", at = @At("HEAD"), cancellable = true)
    public void onGetProfession(CallbackInfoReturnable<Integer> cir) {
        if (this.villager != null && VillagerTradeManager.isCustomizableVillager(this.villager)) {
            VillagerProfession prof = VillagerTradeManager.getProfession(this.villager);
            if (prof == null || this.career <= 0) {
                cir.setReturnValue(-1);
            }
        }
    }

    @Inject(method = "getCareer", at = @At("HEAD"), cancellable = true)
    public void onGetCareer(CallbackInfoReturnable<Integer> cir) {
        if (this.villager != null && VillagerTradeManager.isCustomizableVillager(this.villager)) {
            if (this.career <= 0) {
                cir.setReturnValue(0);
            }
        }
    }

    @Inject(method = "getProfessionLevel", at = @At("HEAD"), cancellable = true)
    public void onGetProfessionLevel(CallbackInfoReturnable<Integer> cir) {
        if (this.villager != null && VillagerTradeManager.isCustomizableVillager(this.villager)) {
            VillagerProfession prof = VillagerTradeManager.getProfession(this.villager);
            if (prof == null || this.career <= 0) {
                cir.setReturnValue(0);
            }
        }
    }

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
