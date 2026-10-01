package com.marrybye.villagertradesbackport.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import com.marrybye.villagertradesbackport.client.gui.GuiGrindstone;

import cpw.mods.fml.common.network.IGuiHandler;

public class ModGuiHandler implements IGuiHandler {

    public static final int GUI_GRINDSTONE = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GUI_GRINDSTONE) {
            return new ContainerGrindstone(player.inventory, world, x, y, z);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (id == GUI_GRINDSTONE) {
            return new GuiGrindstone(player.inventory, world, x, y, z);
        }
        return null;
    }
}
