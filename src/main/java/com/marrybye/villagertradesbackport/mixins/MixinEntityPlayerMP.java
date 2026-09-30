package com.marrybye.villagertradesbackport.mixins;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.marrybye.villagertradesbackport.container.ContainerVillager;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP {

    @Redirect(
        method = "displayGUIMerchant",
        at = @At(value = "NEW", target = "net/minecraft/inventory/ContainerMerchant"))
    private ContainerMerchant villagertrades$redirectContainerMerchant(InventoryPlayer inv, IMerchant merchant,
        World world) {
        return new ContainerVillager(inv, merchant, world);
    }
}
