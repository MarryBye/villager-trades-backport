package com.marrybye.villagertradesbackport.client.renderer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemRenderLectern implements IItemRenderer {

    private final TileEntityLecternRenderer renderer;

    public ItemRenderLectern(TileEntityLecternRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5F, 0.3F, 0.5F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
        } else if (type == ItemRenderType.EQUIPPED) {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            GL11.glScalef(0.7F, 0.7F, 0.7F);
        } else if (type == ItemRenderType.INVENTORY) {
            GL11.glTranslatef(0.0F, -0.15F, 0.0F);
            GL11.glScalef(0.75F, 0.75F, 0.75F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
        } else if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(0.0F, -0.2F, 0.0F);
            GL11.glScalef(0.6F, 0.6F, 0.6F);
        }
        renderer.renderLectern(null, -0.5, -0.5, -0.5, 2);
        GL11.glPopMatrix();
    }
}
