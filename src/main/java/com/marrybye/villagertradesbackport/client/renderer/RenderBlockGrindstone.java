package com.marrybye.villagertradesbackport.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import com.marrybye.villagertradesbackport.block.BlockGrindstone;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockGrindstone implements ISimpleBlockRenderingHandler {

    private final int renderId;

    public RenderBlockGrindstone(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        if (!(block instanceof BlockGrindstone)) return false;
        BlockGrindstone grindstone = (BlockGrindstone) block;
        int meta = world.getBlockMetadata(x, y, z);

        boolean eastWest = (meta == 4 || meta == 5);

        // 1. Legs (wood)
        renderer.setOverrideBlockTexture(grindstone.legIcon);
        if (eastWest) {
            // Leg 1
            renderer.setRenderBounds(6.0 / 16.0, 0.0, 2.0 / 16.0, 10.0 / 16.0, 7.0 / 16.0, 4.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
            // Leg 2
            renderer.setRenderBounds(6.0 / 16.0, 0.0, 12.0 / 16.0, 10.0 / 16.0, 7.0 / 16.0, 14.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            // Leg 1
            renderer.setRenderBounds(2.0 / 16.0, 0.0, 6.0 / 16.0, 4.0 / 16.0, 7.0 / 16.0, 10.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
            // Leg 2
            renderer.setRenderBounds(12.0 / 16.0, 0.0, 6.0 / 16.0, 14.0 / 16.0, 7.0 / 16.0, 10.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
        }

        // 2. Pivots (dark brackets)
        renderer.setOverrideBlockTexture(grindstone.pivotIcon);
        if (eastWest) {
            // Pivot 1
            renderer.setRenderBounds(5.0 / 16.0, 7.0 / 16.0, 2.0 / 16.0, 11.0 / 16.0, 13.0 / 16.0, 4.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
            // Pivot 2
            renderer.setRenderBounds(5.0 / 16.0, 7.0 / 16.0, 12.0 / 16.0, 11.0 / 16.0, 13.0 / 16.0, 14.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
        } else {
            // Pivot 1
            renderer.setRenderBounds(2.0 / 16.0, 7.0 / 16.0, 5.0 / 16.0, 4.0 / 16.0, 13.0 / 16.0, 11.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
            // Pivot 2
            renderer.setRenderBounds(12.0 / 16.0, 7.0 / 16.0, 5.0 / 16.0, 14.0 / 16.0, 13.0 / 16.0, 11.0 / 16.0);
            renderer.renderStandardBlock(block, x, y, z);
        }

        // 3. Wheel (stone round + sides)
        renderer.clearOverrideBlockTexture();
        if (eastWest) {
            renderer.setRenderBounds(2.0 / 16.0, 4.0 / 16.0, 4.0 / 16.0, 14.0 / 16.0, 1.0, 12.0 / 16.0);
        } else {
            renderer.setRenderBounds(4.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0, 12.0 / 16.0, 1.0, 14.0 / 16.0);
        }
        renderer.renderStandardBlock(block, x, y, z);

        renderer.clearOverrideBlockTexture();
        return true;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        if (!(block instanceof BlockGrindstone)) return;
        BlockGrindstone grindstone = (BlockGrindstone) block;

        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        // Render Legs
        renderer.setOverrideBlockTexture(grindstone.legIcon);
        renderBox(renderer, block, 2.0 / 16.0, 0.0, 6.0 / 16.0, 4.0 / 16.0, 7.0 / 16.0, 10.0 / 16.0, tessellator);
        renderBox(renderer, block, 12.0 / 16.0, 0.0, 6.0 / 16.0, 14.0 / 16.0, 7.0 / 16.0, 10.0 / 16.0, tessellator);

        // Render Pivots
        renderer.setOverrideBlockTexture(grindstone.pivotIcon);
        renderBox(
            renderer,
            block,
            2.0 / 16.0,
            7.0 / 16.0,
            5.0 / 16.0,
            4.0 / 16.0,
            13.0 / 16.0,
            11.0 / 16.0,
            tessellator);
        renderBox(
            renderer,
            block,
            12.0 / 16.0,
            7.0 / 16.0,
            5.0 / 16.0,
            14.0 / 16.0,
            13.0 / 16.0,
            11.0 / 16.0,
            tessellator);

        // Render Wheel
        renderer.clearOverrideBlockTexture();
        renderBox(renderer, block, 4.0 / 16.0, 4.0 / 16.0, 2.0 / 16.0, 12.0 / 16.0, 1.0, 14.0 / 16.0, tessellator);

        renderer.clearOverrideBlockTexture();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
    }

    private void renderBox(RenderBlocks renderer, Block block, double minX, double minY, double minZ, double maxX,
        double maxY, double maxZ, Tessellator tessellator) {
        renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 0, 2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 1, 2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 2, 2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 3, 2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 4, 2));
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, renderer.getBlockIconFromSideAndMetadata(block, 5, 2));
        tessellator.draw();
    }
}
