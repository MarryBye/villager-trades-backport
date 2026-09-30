package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.marrybye.villagertradesbackport.gui.GuiVillager;

@Mixin(GuiMerchant.class)
public abstract class MixinGuiMerchant extends GuiContainer {

    public MixinGuiMerchant(Container container) {
        super(container);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void villagertrades$onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if ((Object) this instanceof GuiVillager) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            ci.cancel();
        }
    }

    @Inject(method = "updateScreen", at = @At("HEAD"), cancellable = true)
    private void villagertrades$onUpdateScreen(CallbackInfo ci) {
        if ((Object) this instanceof GuiVillager) {
            ci.cancel();
        }
    }
}
