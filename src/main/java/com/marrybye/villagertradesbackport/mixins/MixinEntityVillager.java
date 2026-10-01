package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.marrybye.villagertradesbackport.trade.VillagerProfession;
import com.marrybye.villagertradesbackport.trade.VillagerTradeManager;

@Mixin(EntityVillager.class)
public abstract class MixinEntityVillager {

    @Inject(method = "addDefaultEquipmentAndRecipies", at = @At("HEAD"), cancellable = true)
    private void onAddDefaultEquipmentAndRecipies(int count, CallbackInfo ci) {
        EntityVillager villager = (EntityVillager) (Object) this;
        if (VillagerTradeManager.isCustomizableVillager(villager)) {
            VillagerTradeManager.initVillagerTrades(villager);
            ci.cancel();
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void onInteract(EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        EntityVillager villager = (EntityVillager) (Object) this;
        if (VillagerTradeManager.isCustomizableVillager(villager)) {
            VillagerProfession prof = VillagerTradeManager.getProfession(villager);
            if (prof == VillagerProfession.NITWIT) {
                villager.worldObj.playSoundAtEntity(villager, "mob.villager.no", 1.0F, 1.0F);
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "useRecipe", at = @At("TAIL"))
    private void onUseRecipe(MerchantRecipe recipe, CallbackInfo ci) {
        EntityVillager villager = (EntityVillager) (Object) this;
        VillagerTradeManager.onTradeUsed(villager, recipe, villager.getCustomer());
    }
}
