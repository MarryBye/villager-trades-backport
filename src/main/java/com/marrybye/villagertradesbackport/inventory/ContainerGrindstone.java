package com.marrybye.villagertradesbackport.inventory;

import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerGrindstone extends Container {

    private final IInventory inputSlots = new InventoryBasic("Grindstone", true, 2) {

        @Override
        public void markDirty() {
            super.markDirty();
            ContainerGrindstone.this.onCraftMatrixChanged(this);
        }
    };
    private final IInventory outputSlot = new InventoryCraftResult();
    private final World world;
    private final int x, y, z;
    private int xpOutput = 0;

    public ContainerGrindstone(InventoryPlayer playerInv, World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;

        // Input slots (Mojang grindstone.png layout)
        this.addSlotToContainer(new Slot(this.inputSlots, 0, 49, 19));
        this.addSlotToContainer(new Slot(this.inputSlots, 1, 49, 40));

        // Output slot
        this.addSlotToContainer(new Slot(this.outputSlot, 2, 129, 34) {

            @Override
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            @Override
            public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
                if (!world.isRemote) {
                    int xp = ContainerGrindstone.this.xpOutput;
                    while (xp > 0) {
                        int split = EntityXPOrb.getXPSplit(xp);
                        xp -= split;
                        world.spawnEntityInWorld(
                            new EntityXPOrb(
                                world,
                                ContainerGrindstone.this.x + 0.5D,
                                ContainerGrindstone.this.y + 0.5D,
                                ContainerGrindstone.this.z + 0.5D,
                                split));
                    }
                    world.playSoundEffect(
                        ContainerGrindstone.this.x + 0.5D,
                        ContainerGrindstone.this.y + 0.5D,
                        ContainerGrindstone.this.z + 0.5D,
                        "random.anvil_use",
                        0.5F,
                        1.0F);
                }

                ContainerGrindstone.this.inputSlots.decrStackSize(0, 1);
                ContainerGrindstone.this.inputSlots.decrStackSize(1, 1);
                ContainerGrindstone.this.xpOutput = 0;
            }
        });

        // Player Inventory (3 rows x 9 columns)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Hotbar (1 row x 9 columns)
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        super.onCraftMatrixChanged(inv);
        if (inv == this.inputSlots) {
            updateOutput();
        }
    }

    private void updateOutput() {
        ItemStack item0 = this.inputSlots.getStackInSlot(0);
        ItemStack item1 = this.inputSlots.getStackInSlot(1);

        if (item0 == null && item1 == null) {
            this.outputSlot.setInventorySlotContents(0, null);
            this.xpOutput = 0;
            return;
        }

        int totalXp = 0;

        // Case 1: Single item disenchanting
        if (item0 != null && item1 == null) {
            totalXp += calculateXp(item0);
            if (totalXp > 0 || (item0.isItemDamaged() && item0.getItem() == Items.enchanted_book)) {
                ItemStack result = removeEnchantments(item0);
                this.outputSlot.setInventorySlotContents(0, result);
                this.xpOutput = totalXp;
                return;
            }
        } else if (item1 != null && item0 == null) {
            totalXp += calculateXp(item1);
            if (totalXp > 0 || (item1.isItemDamaged() && item1.getItem() == Items.enchanted_book)) {
                ItemStack result = removeEnchantments(item1);
                this.outputSlot.setInventorySlotContents(0, result);
                this.xpOutput = totalXp;
                return;
            }
        }

        // Case 2: Combining two items of the same type
        if (item0 != null && item1 != null && item0.getItem() == item1.getItem()) {
            totalXp += calculateXp(item0);
            totalXp += calculateXp(item1);

            if (item0.isItemStackDamageable()) {
                int max = item0.getMaxDamage();
                int rem0 = max - item0.getItemDamage();
                int rem1 = max - item1.getItemDamage();
                int bonus = (int) (max * 0.05F);
                int totalRem = rem0 + rem1 + bonus;
                int newDamage = Math.max(0, max - totalRem);

                ItemStack result = removeEnchantments(item0);
                result.setItemDamage(newDamage);
                this.outputSlot.setInventorySlotContents(0, result);
                this.xpOutput = totalXp;
                return;
            }
        }

        this.outputSlot.setInventorySlotContents(0, null);
        this.xpOutput = 0;
    }

    @SuppressWarnings("unchecked")
    private int calculateXp(ItemStack stack) {
        if (stack == null) return 0;
        int xp = 0;
        Map<Integer, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
        for (Map.Entry<Integer, Integer> entry : enchants.entrySet()) {
            int enchId = entry.getKey();
            int lvl = entry.getValue();
            Enchantment ench = Enchantment.enchantmentsList[enchId];
            if (ench != null) {
                xp += lvl * 2 + ench.getMinEnchantability(lvl) / 2;
            }
        }
        return xp;
    }

    private ItemStack removeEnchantments(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getItem() == Items.enchanted_book) {
            return new ItemStack(Items.book);
        }
        ItemStack result = stack.copy();
        result.stackSize = 1;
        if (result.getTagCompound() != null) {
            result.getTagCompound()
                .removeTag("ench");
            result.getTagCompound()
                .removeTag("StoredEnchantments");
            if (result.getTagCompound()
                .hasNoTags()) {
                result.setTagCompound(null);
            }
        }
        return result;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return player.getDistanceSq(this.x + 0.5D, this.y + 0.5D, this.z + 0.5D) <= 64.0D;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);
        if (!this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                ItemStack stack = this.inputSlots.getStackInSlotOnClosing(i);
                if (stack != null) {
                    player.dropPlayerItemWithRandomChoice(stack, false);
                }
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            itemstack = stackInSlot.copy();

            if (slotIndex == 2) { // Output slot
                if (!this.mergeItemStack(stackInSlot, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(stackInSlot, itemstack);
            } else if (slotIndex == 0 || slotIndex == 1) { // Input slots
                if (!this.mergeItemStack(stackInSlot, 3, 39, false)) {
                    return null;
                }
            } else { // Player inventory
                if (!this.mergeItemStack(stackInSlot, 0, 2, false)) {
                    return null;
                }
            }

            if (stackInSlot.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (stackInSlot.stackSize == itemstack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, stackInSlot);
        }

        return itemstack;
    }
}
