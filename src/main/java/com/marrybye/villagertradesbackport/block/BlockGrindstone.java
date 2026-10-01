package com.marrybye.villagertradesbackport.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.marrybye.villagertradesbackport.VillagerTradesBackport;
import com.marrybye.villagertradesbackport.inventory.ModGuiHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrindstone extends BlockContainer {

    @SideOnly(Side.CLIENT)
    public IIcon roundIcon;
    @SideOnly(Side.CLIENT)
    public IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    public IIcon pivotIcon;
    @SideOnly(Side.CLIENT)
    public IIcon legIcon;

    public BlockGrindstone() {
        super(Material.iron);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setStepSound(soundTypeAnvil);
        this.setBlockName("villagertradesbackport.grindstone");
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityGrindstone();
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        // side: 0=Bottom (ceiling), 1=Top (floor), 2=North, 3=South, 4=West, 5=East
        if (side == 0) {
            return 2; // Ceiling default (North/South)
        } else if (side == 1) {
            return 0; // Floor default (North/South)
        } else if (side == 2) {
            return 4; // Wall: clicked North face
        } else if (side == 3) {
            return 5; // Wall: clicked South face
        } else if (side == 4) {
            return 6; // Wall: clicked West face
        } else if (side == 5) {
            return 7; // Wall: clicked East face
        }
        return 0;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        int meta = world.getBlockMetadata(x, y, z);
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        boolean eastWest = (l == 1 || l == 3);

        if (meta == 0 || meta == 1) { // Floor
            world.setBlockMetadataWithNotify(x, y, z, eastWest ? 1 : 0, 2);
        } else if (meta == 2 || meta == 3) { // Ceiling
            world.setBlockMetadataWithNotify(x, y, z, eastWest ? 3 : 2, 2);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == 0 || meta == 1) { // Floor
            this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 0.875F, 0.875F);
        } else if (meta == 2 || meta == 3) { // Ceiling
            this.setBlockBounds(0.125F, 0.125F, 0.125F, 0.875F, 1.0F, 0.875F);
        } else if (meta == 4) { // Wall: North face clicked, attached to South wall
            this.setBlockBounds(0.125F, 0.125F, 0.125F, 0.875F, 0.875F, 1.0F);
        } else if (meta == 5) { // Wall: South face clicked, attached to North wall
            this.setBlockBounds(0.125F, 0.125F, 0.0F, 0.875F, 0.875F, 0.875F);
        } else if (meta == 6) { // Wall: West face clicked, attached to East wall
            this.setBlockBounds(0.125F, 0.125F, 0.125F, 1.0F, 0.875F, 0.875F);
        } else if (meta == 7) { // Wall: East face clicked, attached to West wall
            this.setBlockBounds(0.0F, 0.125F, 0.125F, 0.875F, 0.875F, 0.875F);
        } else {
            this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX,
        float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(VillagerTradesBackport.instance, ModGuiHandler.GUI_GRINDSTONE, world, x, y, z);
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return sideIcon != null ? sideIcon : roundIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.roundIcon = register.registerIcon("villagertradesbackport:grindstone_round");
        this.sideIcon = register.registerIcon("villagertradesbackport:grindstone_side");
        this.pivotIcon = register.registerIcon("villagertradesbackport:grindstone_pivot");
        this.legIcon = register.registerIcon("villagertradesbackport:grindstone_leg");
    }
}
