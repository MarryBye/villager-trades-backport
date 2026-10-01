package com.marrybye.villagertradesbackport.client.renderer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.marrybye.villagertradesbackport.block.BlockGrindstone;
import com.marrybye.villagertradesbackport.block.ModBlocks;

public class TileEntityGrindstoneRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 0;
        renderGrindstone(tile, x, y, z, meta);
    }

    public void renderGrindstone(TileEntity tile, double x, double y, double z, int meta) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        float rotX = 0.0F;
        float rotY = 0.0F;
        switch (meta) {
            case 0: // Floor North/South
                rotX = 0.0F;
                rotY = 0.0F;
                break;
            case 1: // Floor East/West
                rotX = 0.0F;
                rotY = 90.0F;
                break;
            case 2: // Ceiling North/South
                rotX = 180.0F;
                rotY = 0.0F;
                break;
            case 3: // Ceiling East/West
                rotX = 180.0F;
                rotY = 90.0F;
                break;
            case 4: // Wall: North face of clicked block (attached to South wall)
                rotX = 90.0F;
                rotY = 180.0F;
                break;
            case 5: // Wall: South face of clicked block (attached to North wall)
                rotX = 90.0F;
                rotY = 0.0F;
                break;
            case 6: // Wall: West face of clicked block (attached to East wall)
                rotX = 90.0F;
                rotY = 270.0F;
                break;
            case 7: // Wall: East face of clicked block (attached to West wall)
                rotX = 90.0F;
                rotY = 90.0F;
                break;
            default:
                break;
        }

        GL11.glRotatef(rotY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(rotX, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -0.5F, 0.0F);

        this.bindTexture(TextureMap.locationBlocksTexture);

        BlockGrindstone block = ModBlocks.grindstone;
        IIcon legIcon = (block != null && block.legIcon != null) ? block.legIcon : Blocks.planks.getIcon(0, 0);
        IIcon pivotIcon = (block != null && block.pivotIcon != null) ? block.pivotIcon
            : Blocks.iron_block.getIcon(0, 0);
        IIcon roundIcon = (block != null && block.roundIcon != null) ? block.roundIcon : Blocks.stone.getIcon(0, 0);
        IIcon sideIcon = (block != null && block.sideIcon != null) ? block.sideIcon : Blocks.stone.getIcon(0, 0);

        Tessellator tess = Tessellator.instance;

        // 1. Leg 1 (East Leg): [12, 0, 6] to [14, 7, 10]
        // Model coords: [0.25, 0.0, -0.125] to [0.375, 0.4375, 0.125]
        tess.startDrawingQuads();
        renderFaceNorth(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 2, 9, 4, 16);
        renderFaceSouth(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 12, 9, 14, 16);
        renderFaceEast(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 6, 9, 10, 16);
        renderFaceWest(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 6, 9, 10, 16);
        renderFaceDown(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 12, 6, 14, 10);
        renderFaceUp(tess, 0.25, 0.0, -0.125, 0.375, 0.4375, 0.125, legIcon, 12, 6, 14, 10);
        tess.draw();

        // 2. Leg 2 (West Leg): [2, 0, 6] to [4, 7, 10]
        // Model coords: [-0.375, 0.0, -0.125] to [-0.25, 0.4375, 0.125]
        tess.startDrawingQuads();
        renderFaceNorth(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 12, 9, 14, 16);
        renderFaceSouth(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 2, 9, 4, 16);
        renderFaceEast(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 6, 9, 10, 16);
        renderFaceWest(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 6, 9, 10, 16);
        renderFaceDown(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 2, 6, 4, 10);
        renderFaceUp(tess, -0.375, 0.0, -0.125, -0.25, 0.4375, 0.125, legIcon, 2, 6, 4, 10);
        tess.draw();

        // 3. Pivot 1 (East Pivot): [12, 7, 5] to [14, 13, 11]
        // Model coords: [0.25, 0.4375, -0.1875] to [0.375, 0.8125, 0.1875]
        tess.startDrawingQuads();
        renderFaceEast(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 0, 0, 6, 6);
        renderFaceWest(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 0, 0, 6, 6);
        renderFaceNorth(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 6, 0, 8, 6);
        renderFaceSouth(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 6, 0, 8, 6);
        renderFaceUp(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 8, 0, 10, 6);
        renderFaceDown(tess, 0.25, 0.4375, -0.1875, 0.375, 0.8125, 0.1875, pivotIcon, 8, 0, 10, 6);
        tess.draw();

        // 4. Pivot 2 (West Pivot): [2, 7, 5] to [4, 13, 11]
        // Model coords: [-0.375, 0.4375, -0.1875] to [-0.25, 0.8125, 0.1875]
        tess.startDrawingQuads();
        renderFaceWest(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 0, 0, 6, 6);
        renderFaceEast(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 0, 0, 6, 6);
        renderFaceNorth(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 6, 0, 8, 6);
        renderFaceSouth(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 6, 0, 8, 6);
        renderFaceUp(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 8, 0, 10, 6);
        renderFaceDown(tess, -0.375, 0.4375, -0.1875, -0.25, 0.8125, 0.1875, pivotIcon, 8, 0, 10, 6);
        tess.draw();

        // 5. Wheel: [4, 4, 2] to [12, 16, 14]
        // Model coords: [-0.25, 0.25, -0.375] to [0.25, 1.0, 0.375]
        tess.startDrawingQuads();
        renderFaceNorth(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, roundIcon, 0, 0, 8, 12);
        renderFaceSouth(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, roundIcon, 0, 0, 8, 12);
        renderFaceUp(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, roundIcon, 0, 0, 8, 12);
        renderFaceDown(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, roundIcon, 0, 0, 8, 12);
        renderFaceEast(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, sideIcon, 0, 0, 12, 12);
        renderFaceWest(tess, -0.25, 0.25, -0.375, 0.25, 1.0, 0.375, sideIcon, 0, 0, 12, 12);
        tess.draw();

        GL11.glPopMatrix();
    }

    private void renderFaceDown(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(0.0F, -1.0F, 0.0F);
        tess.setColorOpaque_F(0.5F, 0.5F, 0.5F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, minZ, minU, minV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
    }

    private void renderFaceUp(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(0.0F, 1.0F, 0.0F);
        tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
    }

    private void renderFaceNorth(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(0.0F, 0.0F, -1.0F);
        tess.setColorOpaque_F(0.8F, 0.8F, 0.8F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(maxX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, minZ, minU, minV);
    }

    private void renderFaceSouth(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.setColorOpaque_F(0.8F, 0.8F, 0.8F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(minX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, maxZ, minU, minV);
    }

    private void renderFaceWest(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        tess.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(minX, minY, minZ, minU, maxV);
        tess.addVertexWithUV(minX, minY, maxZ, maxU, maxV);
        tess.addVertexWithUV(minX, maxY, maxZ, maxU, minV);
        tess.addVertexWithUV(minX, maxY, minZ, minU, minV);
    }

    private void renderFaceEast(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon icon, double u1, double v1, double u2, double v2) {
        tess.setNormal(1.0F, 0.0F, 0.0F);
        tess.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        double minU = icon.getInterpolatedU(u1);
        double maxU = icon.getInterpolatedU(u2);
        double minV = icon.getInterpolatedV(v1);
        double maxV = icon.getInterpolatedV(v2);
        tess.addVertexWithUV(maxX, minY, maxZ, minU, maxV);
        tess.addVertexWithUV(maxX, minY, minZ, maxU, maxV);
        tess.addVertexWithUV(maxX, maxY, minZ, maxU, minV);
        tess.addVertexWithUV(maxX, maxY, maxZ, minU, minV);
    }
}
