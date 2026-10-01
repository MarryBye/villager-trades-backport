package com.marrybye.villagertradesbackport.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.marrybye.villagertradesbackport.VillagerTradesBackport;
import com.marrybye.villagertradesbackport.inventory.ModGuiHandler;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockGrindstone extends Block {

    @SideOnly(Side.CLIENT)
    public IIcon roundIcon;
    @SideOnly(Side.CLIENT)
    public IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    public IIcon pivotIcon;
    @SideOnly(Side.CLIENT)
    public IIcon legIcon;

    private int renderId;

    public BlockGrindstone() {
        super(Material.iron);
        this.setHardness(2.0F);
        this.setResistance(6.0F);
        this.setStepSound(soundTypeAnvil);
        this.setBlockName("villagertradesbackport.grindstone");
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.125F, 0.0F, 0.125F, 0.875F, 1.0F, 0.875F);
    }

    public void setRenderId(int id) {
        this.renderId = id;
    }

    @Override
    public int getRenderType() {
        return this.renderId;
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (l == 0) world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        if (l == 1) world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        if (l == 2) world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        if (l == 3) world.setBlockMetadataWithNotify(x, y, z, 4, 2);
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
        if (meta == 4 || meta == 5) {
            if (side == 2 || side == 3) return sideIcon;
            return roundIcon;
        } else {
            if (side == 4 || side == 5) return sideIcon;
            return roundIcon;
        }
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
