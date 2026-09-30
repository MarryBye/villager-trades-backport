package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EntityVillager.class)
public interface AccessorEntityVillager {

    @Accessor("timeUntilReset")
    int getTimeUntilReset();

    @Accessor("timeUntilReset")
    void setTimeUntilReset(int timeUntilReset);

    @Accessor("needsInitilization")
    boolean getNeedsInitilization();

    @Accessor("needsInitilization")
    void setNeedsInitilization(boolean needsInitilization);

    @Accessor("buyingList")
    MerchantRecipeList getBuyingList();

    @Accessor("buyingList")
    void setBuyingList(MerchantRecipeList buyingList);

    @Invoker("addDefaultEquipmentAndRecipies")
    void invokeAddDefaultEquipmentAndRecipies(int count);
}
