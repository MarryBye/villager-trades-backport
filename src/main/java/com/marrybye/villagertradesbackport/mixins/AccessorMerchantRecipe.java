package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.village.MerchantRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantRecipe.class)
public interface AccessorMerchantRecipe {

    @Accessor("toolUses")
    int getToolUses();

    @Accessor("toolUses")
    void setToolUses(int toolUses);

    @Accessor("maxTradeUses")
    int getMaxTradeUses();

    @Accessor("maxTradeUses")
    void setMaxTradeUses(int maxTradeUses);
}
