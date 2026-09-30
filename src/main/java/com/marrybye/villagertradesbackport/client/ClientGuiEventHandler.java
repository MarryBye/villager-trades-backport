package com.marrybye.villagertradesbackport.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;

import com.marrybye.villagertradesbackport.container.ContainerVillager;
import com.marrybye.villagertradesbackport.gui.GuiVillager;
import com.marrybye.villagertradesbackport.mixins.AccessorGuiMerchant;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ClientGuiEventHandler {

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMerchant && !(event.gui instanceof GuiVillager)) {
            GuiMerchant oldGui = (GuiMerchant) event.gui;
            AccessorGuiMerchant accessor = (AccessorGuiMerchant) oldGui;
            IMerchant merchant = accessor.getMerchant();
            String title = accessor.getTitle();

            InventoryPlayer playerInv = Minecraft.getMinecraft().thePlayer.inventory;
            ContainerVillager container = new ContainerVillager(playerInv, merchant, Minecraft.getMinecraft().theWorld);
            container.windowId = oldGui.inventorySlots.windowId;

            event.gui = new GuiVillager(container, merchant, title);
        }
    }
}
