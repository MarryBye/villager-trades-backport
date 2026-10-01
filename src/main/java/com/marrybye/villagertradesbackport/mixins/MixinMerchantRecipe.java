package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.village.MerchantRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.marrybye.villagertradesbackport.trade.ITradeOffer;

@Mixin(MerchantRecipe.class)
public abstract class MixinMerchantRecipe implements ITradeOffer {

    @Unique
    private int vtb$villagerXp = 1;

    @Unique
    private float vtb$priceMultiplier = 0.05F;

    @Unique
    private int vtb$demand = 0;

    @Override
    public int getVillagerXp() {
        return this.vtb$villagerXp;
    }

    @Override
    public void setVillagerXp(int xp) {
        this.vtb$villagerXp = Math.max(1, xp);
    }

    @Override
    public float getPriceMultiplier() {
        return this.vtb$priceMultiplier;
    }

    @Override
    public void setPriceMultiplier(float priceMultiplier) {
        this.vtb$priceMultiplier = priceMultiplier;
    }

    @Override
    public int getDemand() {
        return this.vtb$demand;
    }

    @Override
    public void setDemand(int demand) {
        this.vtb$demand = demand;
    }

    @Inject(method = "readFromTags", at = @At("RETURN"))
    private void onReadFromTags(NBTTagCompound tag, CallbackInfo ci) {
        if (tag.hasKey("VTB_xp")) {
            this.vtb$villagerXp = tag.getInteger("VTB_xp");
        }
        if (tag.hasKey("VTB_priceMultiplier")) {
            this.vtb$priceMultiplier = tag.getFloat("VTB_priceMultiplier");
        }
        if (tag.hasKey("VTB_demand")) {
            this.vtb$demand = tag.getInteger("VTB_demand");
        }
    }

    @Inject(method = "writeToTags", at = @At("RETURN"))
    private void onWriteToTags(CallbackInfoReturnable<NBTTagCompound> cir) {
        NBTTagCompound tag = cir.getReturnValue();
        if (tag != null) {
            tag.setInteger("VTB_xp", this.vtb$villagerXp);
            tag.setFloat("VTB_priceMultiplier", this.vtb$priceMultiplier);
            tag.setInteger("VTB_demand", this.vtb$demand);
        }
    }
}
