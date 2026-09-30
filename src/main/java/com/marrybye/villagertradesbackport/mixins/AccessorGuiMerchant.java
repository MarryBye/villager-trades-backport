package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiMerchant.class)
public interface AccessorGuiMerchant {

    @Accessor("field_147037_w")
    IMerchant getMerchant();

    @Accessor("field_147040_A")
    String getTitle();
}
