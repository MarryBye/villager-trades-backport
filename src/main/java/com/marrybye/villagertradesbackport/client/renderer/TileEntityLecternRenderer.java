package com.marrybye.villagertradesbackport.client.renderer;

import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.marrybye.villagertradesbackport.block.BlockLectern;
import com.marrybye.villagertradesbackport.block.ModBlocks;
import com.marrybye.villagertradesbackport.block.TileEntityLectern;

public class TileEntityLecternRenderer extends TileEntitySpecialRenderer {

    private static final ResourceLocation BOOK_TEXTURE = new ResourceLocation(
        "textures/entity/enchanting_table_book.png");
    private final ModelBook bookModel = new ModelBook();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileEntityLectern lectern = (tile instanceof TileEntityLectern) ? (TileEntityLectern) tile : null;
        int meta = tile.hasWorldObj() ? tile.getBlockMetadata() : 2;

        renderLectern(lectern, x, y, z, meta);
    }

    public void renderLectern(TileEntityLectern lectern, double x, double y, double z, int meta) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);

        // Rotation based on metadata: 2=North, 3=South, 4=West, 5=East
        float rotation = 0.0F;
        if (meta == 2) rotation = 0.0F;
        else if (meta == 3) rotation = 180.0F;
        else if (meta == 4) rotation = 90.0F;
        else if (meta == 5) rotation = 270.0F;
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

        this.bindTexture(TextureMap.locationBlocksTexture);

        BlockLectern block = ModBlocks.lectern;
        IIcon baseIcon = (block != null && block.baseIcon != null) ? block.baseIcon : Blocks.planks.getIcon(0, 0);
        IIcon sideIcon = (block != null && block.sideIcon != null) ? block.sideIcon : Blocks.planks.getIcon(0, 0);
        IIcon topIcon = (block != null && block.topIcon != null) ? block.topIcon : Blocks.planks.getIcon(0, 0);
        IIcon bottomIcon = Blocks.planks.getIcon(0, 0);

        Tessellator tess = Tessellator.instance;

        // 1. Base: 16 x 2 x 16 (y: 0.0 to 2/16)
        // Mojang lectern.json:
        // down: [0, 0, 16, 16] planks
        // up: [0, 0, 16, 16] base
        // north: [0, 14, 16, 16] base
        // south/east/west: [0, 6, 16, 8] base
        tess.startDrawingQuads();
        renderFaceDown(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, bottomIcon, 0, 0, 16, 16);
        renderFaceUp(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, baseIcon, 0, 0, 16, 16);
        renderFaceNorth(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, baseIcon, 0, 14, 16, 16);
        renderFaceSouth(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, baseIcon, 0, 6, 16, 8);
        renderFaceEast(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, baseIcon, 0, 6, 16, 8);
        renderFaceWest(tess, -0.5, 0.0, -0.5, 0.5, 0.125, 0.5, baseIcon, 0, 6, 16, 8);
        tess.draw();

        // 2. Pillar: [4, 2, 4] to [12, 15, 12] (8 x 13 x 8, y: 2/16 to 15/16)
        // Using authentic 1:1 pixel wood mapping from sideIcon (8x13):
        tess.startDrawingQuads();
        renderFaceNorth(tess, -0.25, 0.125, -0.25, 0.25, 0.9375, 0.25, sideIcon, 4, 2, 12, 15);
        renderFaceSouth(tess, -0.25, 0.125, -0.25, 0.25, 0.9375, 0.25, sideIcon, 4, 2, 12, 15);
        renderFaceEast(tess, -0.25, 0.125, -0.25, 0.25, 0.9375, 0.25, sideIcon, 4, 2, 12, 15);
        renderFaceWest(tess, -0.25, 0.125, -0.25, 0.25, 0.9375, 0.25, sideIcon, 4, 2, 12, 15);
        tess.draw();

        // 3. Slanted Desk: tilted around origin (0.0, 8/16, 0.0) by -22.5 degrees
        // Mojang lectern.json:
        // [0, 12, 3] to [16, 16, 16] with origin [8, 8, 8], angle -22.5 on x
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.5F, 0.0F);
        GL11.glRotatef(-22.5F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -0.5F, 0.0F);

        tess.startDrawingQuads();
        // Desk cuboid: x: [-0.5, 0.5], y: [0.75, 1.0], z: [-0.3125, 0.5]
        renderFaceUp(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, topIcon, 0, 1, 16, 14);
        renderFaceDown(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, bottomIcon, 0, 0, 16, 13);
        renderFaceNorth(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, sideIcon, 0, 0, 16, 4);
        renderFaceSouth(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, sideIcon, 0, 4, 16, 8);
        renderFaceEast(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, sideIcon, 0, 4, 13, 8);
        renderFaceWest(tess, -0.5, 0.75, -0.3125, 0.5, 1.0, 0.5, sideIcon, 0, 4, 13, 8);
        tess.draw();

        // 4. Book (if present)
        if (lectern != null && lectern.hasBook()) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 1.03F, 0.08F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            this.bindTexture(BOOK_TEXTURE);
            bookModel.render((Entity) null, 0.0F, 0.1F, 0.9F, 1.2F, 0.0F, 0.0625F);
            GL11.glPopMatrix();
        }

        GL11.glPopMatrix(); // Desk rotation
        GL11.glPopMatrix(); // Lectern transform
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
