package com.marrybye.villagertradesbackport;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import com.marrybye.villagertradesbackport.block.ModBlocks;
import com.marrybye.villagertradesbackport.block.TileEntityGrindstone;
import com.marrybye.villagertradesbackport.block.TileEntityLectern;
import com.marrybye.villagertradesbackport.client.ClientGuiEventHandler;
import com.marrybye.villagertradesbackport.client.renderer.ItemRenderGrindstone;
import com.marrybye.villagertradesbackport.client.renderer.ItemRenderLectern;
import com.marrybye.villagertradesbackport.client.renderer.TileEntityGrindstoneRenderer;
import com.marrybye.villagertradesbackport.client.renderer.TileEntityLecternRenderer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new ClientGuiEventHandler());

        // Register Lectern TESR and 3D item renderer
        TileEntityLecternRenderer lecternRenderer = new TileEntityLecternRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, lecternRenderer);
        MinecraftForgeClient
            .registerItemRenderer(Item.getItemFromBlock(ModBlocks.lectern), new ItemRenderLectern(lecternRenderer));

        // Register Grindstone TESR and 3D item renderer
        TileEntityGrindstoneRenderer grindstoneRenderer = new TileEntityGrindstoneRenderer();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrindstone.class, grindstoneRenderer);
        MinecraftForgeClient.registerItemRenderer(
            Item.getItemFromBlock(ModBlocks.grindstone),
            new ItemRenderGrindstone(grindstoneRenderer));
    }
}
