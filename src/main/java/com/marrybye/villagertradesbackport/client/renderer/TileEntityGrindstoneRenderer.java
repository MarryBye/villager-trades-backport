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
            case 4: // Wall: North face of clicked block (points North, back on South)
                rotX = 90.0F;
                rotY = 180.0F;
                break;
            case 5: // Wall: South face of clicked block (points South, back on North)
                rotX = 90.0F;
                rotY = 0.0F;
                break;
            case 6: // Wall: West face of clicked block (points West, back on East)
                rotX = 90.0F;
                rotY = 270.0F;
                break;
            case 7: // Wall: East face of clicked block (points East, back on West)
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

        // 1. Leg 1: [12, 0, 6] to [14, 7, 10] -> [0.25, 0.0, -0.125] to [0.375, 0.4375, 0.125]
        tess.startDrawingQuads();
        renderCuboid(
            tess,
            0.25,
            0.0,
            -0.125,
            0.375,
            0.4375,
            0.125,
            legIcon,
            legIcon,
            legIcon,
            legIcon,
            legIcon,
            legIcon);
        tess.draw();

        // 2. Leg 2: [2, 0, 6] to [4, 7, 10] -> [-0.375, 0.0, -0.125] to [-0.25, 0.4375, 0.125]
        tess.startDrawingQuads();
        renderCuboid(
            tess,
            -0.375,
            0.0,
            -0.125,
            -0.25,
            0.4375,
            0.125,
            legIcon,
            legIcon,
            legIcon,
            legIcon,
            legIcon,
            legIcon);
        tess.draw();

        // 3. Pivot 1: [12, 7, 5] to [14, 13, 11] -> [0.25, 0.4375, -0.1875] to [0.375, 0.8125, 0.1875]
        tess.startDrawingQuads();
        renderCuboid(
            tess,
            0.25,
            0.4375,
            -0.1875,
            0.375,
            0.8125,
            0.1875,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon);
        tess.draw();

        // 4. Pivot 2: [2, 7, 5] to [4, 13, 11] -> [-0.375, 0.4375, -0.1875] to [-0.25, 0.8125, 0.1875]
        tess.startDrawingQuads();
        renderCuboid(
            tess,
            -0.375,
            0.4375,
            -0.1875,
            -0.25,
            0.8125,
            0.1875,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon,
            pivotIcon);
        tess.draw();

        // 5. Wheel: [4, 4, 2] to [12, 16, 14] -> [-0.25, 0.25, -0.375] to [0.25, 1.0, 0.375]
        tess.startDrawingQuads();
        renderCuboid(
            tess,
            -0.25,
            0.25,
            -0.375,
            0.25,
            1.0,
            0.375,
            roundIcon,
            roundIcon,
            roundIcon,
            roundIcon,
            sideIcon,
            sideIcon);
        tess.draw();

        GL11.glPopMatrix();
    }

    private void renderCuboid(Tessellator tess, double minX, double minY, double minZ, double maxX, double maxY,
        double maxZ, IIcon iconDown, IIcon iconUp, IIcon iconNorth, IIcon iconSouth, IIcon iconWest, IIcon iconEast) {
        // Down (Y-)
        tess.setNormal(0.0F, -1.0F, 0.0F);
        tess.setColorOpaque_F(0.5F, 0.5F, 0.5F);
        tess.addVertexWithUV(minX, minY, maxZ, iconDown.getMinU(), iconDown.getMaxV());
        tess.addVertexWithUV(minX, minY, minZ, iconDown.getMinU(), iconDown.getMinV());
        tess.addVertexWithUV(maxX, minY, minZ, iconDown.getMaxU(), iconDown.getMinV());
        tess.addVertexWithUV(maxX, minY, maxZ, iconDown.getMaxU(), iconDown.getMaxV());

        // Up (Y+)
        tess.setNormal(0.0F, 1.0F, 0.0F);
        tess.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        tess.addVertexWithUV(minX, maxY, minZ, iconUp.getMinU(), iconUp.getMinV());
        tess.addVertexWithUV(minX, maxY, maxZ, iconUp.getMinU(), iconUp.getMaxV());
        tess.addVertexWithUV(maxX, maxY, maxZ, iconUp.getMaxU(), iconUp.getMaxV());
        tess.addVertexWithUV(maxX, maxY, minZ, iconUp.getMaxU(), iconUp.getMinV());

        // North (Z-)
        tess.setNormal(0.0F, 0.0F, -1.0F);
        tess.setColorOpaque_F(0.8F, 0.8F, 0.8F);
        tess.addVertexWithUV(maxX, minY, minZ, iconNorth.getMinU(), iconNorth.getMaxV());
        tess.addVertexWithUV(minX, minY, minZ, iconNorth.getMaxU(), iconNorth.getMaxV());
        tess.addVertexWithUV(minX, maxY, minZ, iconNorth.getMaxU(), iconNorth.getMinV());
        tess.addVertexWithUV(maxX, maxY, minZ, iconNorth.getMinU(), iconNorth.getMinV());

        // South (Z+)
        tess.setNormal(0.0F, 0.0F, 1.0F);
        tess.setColorOpaque_F(0.8F, 0.8F, 0.8F);
        tess.addVertexWithUV(minX, minY, maxZ, iconSouth.getMinU(), iconSouth.getMaxV());
        tess.addVertexWithUV(maxX, minY, maxZ, iconSouth.getMaxU(), iconSouth.getMaxV());
        tess.addVertexWithUV(maxX, maxY, maxZ, iconSouth.getMaxU(), iconSouth.getMinV());
        tess.addVertexWithUV(minX, maxY, maxZ, iconSouth.getMinU(), iconSouth.getMinV());

        // West (X-)
        tess.setNormal(-1.0F, 0.0F, 0.0F);
        tess.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        tess.addVertexWithUV(minX, minY, minZ, iconWest.getMinU(), iconWest.getMaxV());
        tess.addVertexWithUV(minX, minY, maxZ, iconWest.getMaxU(), iconWest.getMaxV());
        tess.addVertexWithUV(minX, maxY, maxZ, iconWest.getMaxU(), iconWest.getMinV());
        tess.addVertexWithUV(minX, maxY, minZ, iconWest.getMinU(), iconWest.getMinV());

        // East (X+)
        tess.setNormal(1.0F, 0.0F, 0.0F);
        tess.setColorOpaque_F(0.6F, 0.6F, 0.6F);
        tess.addVertexWithUV(maxX, minY, maxZ, iconEast.getMinU(), iconEast.getMaxV());
        tess.addVertexWithUV(maxX, minY, minZ, iconEast.getMaxU(), iconEast.getMaxV());
        tess.addVertexWithUV(maxX, maxY, minZ, iconEast.getMaxU(), iconEast.getMinV());
        tess.addVertexWithUV(maxX, maxY, maxZ, iconEast.getMinU(), iconEast.getMinV());
    }
}
