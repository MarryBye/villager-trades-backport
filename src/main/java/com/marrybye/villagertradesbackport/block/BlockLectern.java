package com.marrybye.villagertradesbackport.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLectern extends BlockContainer {

    @SideOnly(Side.CLIENT)
    public IIcon topIcon;
    @SideOnly(Side.CLIENT)
    public IIcon sideIcon;
    @SideOnly(Side.CLIENT)
    public IIcon frontIcon;
    @SideOnly(Side.CLIENT)
    public IIcon baseIcon;

    public BlockLectern() {
        super(Material.wood);
        this.setHardness(2.5F);
        this.setResistance(2.5F);
        this.setStepSound(soundTypeWood);
        this.setBlockName("villagertradesbackport.lectern");
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityLectern();
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
    public int getRenderType() {
        return -1;
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
        TileEntity tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileEntityLectern)) return false;
        TileEntityLectern lectern = (TileEntityLectern) tile;

        if (player.isSneaking()) {
            if (lectern.hasBook()) {
                if (!world.isRemote) {
                    ItemStack book = lectern.getBook();
                    lectern.setBook(null);
                    if (!player.inventory.addItemStackToInventory(book)) {
                        player.dropPlayerItemWithRandomChoice(book, false);
                    }
                    world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "random.pop", 0.5F, 1.0F);
                    world.notifyBlocksOfNeighborChange(x, y, z, this);
                }
                return true;
            }
            return false;
        }

        if (lectern.hasBook()) {
            if (world.isRemote) {
                player.displayGUIBook(lectern.getBook());
            }
            return true;
        }

        ItemStack held = player.getHeldItem();
        if (held != null && (held.getItem() == Items.writable_book || held.getItem() == Items.written_book)) {
            if (!world.isRemote) {
                ItemStack toPlace = held.copy();
                toPlace.stackSize = 1;
                lectern.setBook(toPlace);
                if (!player.capabilities.isCreativeMode) {
                    held.stackSize--;
                    if (held.stackSize <= 0) {
                        player.setCurrentItemOrArmor(0, null);
                    }
                }
                world.playSoundEffect(x + 0.5D, y + 0.5D, z + 0.5D, "item.fireCharge.use", 0.5F, 1.0F);
                world.notifyBlocksOfNeighborChange(x, y, z, this);
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityLectern && ((TileEntityLectern) tile).hasBook()) {
            return 15;
        }
        return 0;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityLectern) {
            TileEntityLectern lectern = (TileEntityLectern) tile;
            if (lectern.hasBook()) {
                float rx = world.rand.nextFloat() * 0.8F + 0.1F;
                float ry = world.rand.nextFloat() * 0.8F + 0.1F;
                float rz = world.rand.nextFloat() * 0.8F + 0.1F;
                EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, lectern.getBook());
                world.spawnEntityInWorld(entityItem);
            }
        }
        super.breakBlock(world, x, y, z, block, meta);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 5;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return 20;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        if (side == 1) return topIcon;
        if (side == 0) return baseIcon;
        if (side == meta) return frontIcon;
        return sideIcon;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        this.topIcon = register.registerIcon("villagertradesbackport:lectern_top");
        this.sideIcon = register.registerIcon("villagertradesbackport:lectern_sides");
        this.frontIcon = register.registerIcon("villagertradesbackport:lectern_front");
        this.baseIcon = register.registerIcon("villagertradesbackport:lectern_base");
    }
}
