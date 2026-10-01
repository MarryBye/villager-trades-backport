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
        IIcon frontIcon = (block != null && block.frontIcon != null) ? block.frontIcon : Blocks.planks.getIcon(0, 0);
        IIcon topIcon = (block != null && block.topIcon != null) ? block.topIcon : Blocks.planks.getIcon(0, 0);
        IIcon bottomIcon = Blocks.planks.getIcon(0, 0);

        Tessellator tessellator = Tessellator.instance;

        // 1. Base: 16 x 2 x 16 (y: 0.0 to 2/16)
        tessellator.startDrawingQuads();
        renderCuboid(
            tessellator,
            -0.5,
            0.0,
            -0.5,
            0.5,
            2.0 / 16.0,
            0.5,
            bottomIcon,
            baseIcon,
            baseIcon,
            baseIcon,
            baseIcon,
            baseIcon);
        tessellator.draw();

        // 2. Pillar: 8 x 12 x 8 (y: 2/16 to 14/16, x: -0.25 to 0.25, z: -0.25 to 0.25)
        // Authentic Mojang 1.14: pillar sides are wood (#sides), not books!
        tessellator.startDrawingQuads();
        renderCuboid(
            tessellator,
            -0.25,
            2.0 / 16.0,
            -0.25,
            0.25,
            14.0 / 16.0,
            0.25,
            bottomIcon,
            topIcon,
            sideIcon,
            sideIcon,
            sideIcon,
            sideIcon);
        tessellator.draw();

        // 3. Slanted Desk: tilted around pivot (0.0, 12/16, 0.0) by -22.5 degrees
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 12.0F / 16.0F, 0.0F);
        GL11.glRotatef(-22.5F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(0.0F, -12.0F / 16.0F, 0.0F);

        tessellator.startDrawingQuads();
        renderCuboid(
            tessellator,
            -0.5,
            12.0 / 16.0,
            -0.3125,
            0.5,
            16.0 / 16.0,
            0.5,
            bottomIcon,
            topIcon,
            sideIcon,
            sideIcon,
            sideIcon,
            sideIcon);
        tessellator.draw();

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
