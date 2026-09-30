package com.marrybye.villagertradesbackport.container;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotMerchantResult;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

import com.marrybye.villagertradesbackport.mixins.AccessorMerchantRecipe;
import com.marrybye.villagertradesbackport.network.ModNetwork;

public class ContainerVillager extends ContainerMerchant {

    private final InventoryPlayer playerInv;
    private final IMerchant merchant;
    private final InventoryMerchant merchantInventory;
    private int[] lastSentUses;

    public ContainerVillager(InventoryPlayer playerInv, IMerchant merchant, World world) {
        super(playerInv, merchant, world);
        this.playerInv = playerInv;
        this.merchant = merchant;
        this.merchantInventory = this.getMerchantInventory();

        // Clear default 176px slot layout from vanilla ContainerMerchant
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        int index = 0;
        // Merchant buy slot 1
        this.addSlotToContainer(new Slot(this.merchantInventory, index++, 136, 37));
        // Merchant buy slot 2
        this.addSlotToContainer(new Slot(this.merchantInventory, index++, 162, 37));
        // Merchant result slot
        this.addSlotToContainer(
            new SlotMerchantResult(playerInv.player, merchant, this.merchantInventory, index++, 220, 37));

        // Player main inventory (3 rows x 9 columns) shifted to the right (x=108)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 108 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar (1 row x 9 columns)
        for (int col = 0; col < 9; ++col) {
            this.addSlotToContainer(new Slot(playerInv, col, 108 + col * 18, 142));
        }
    }

    public IMerchant getMerchant() {
        return this.merchant;
    }

    public InventoryPlayer getPlayerInv() {
        return this.playerInv;
    }

    /**
     * Fast trade helper: moves items required for the selected trade from the player's inventory
     * into the merchant input slots.
     */
    public void moveAroundItems(int tradeIndex) {
        MerchantRecipeList trades = this.merchant.getRecipes(this.playerInv.player);
        if (trades == null || tradeIndex < 0 || tradeIndex >= trades.size()) {
            return;
        }

        // Return current input slot items back to player inventory if present
        ItemStack stack0 = this.merchantInventory.getStackInSlot(0);
        if (stack0 != null && stack0.stackSize > 0) {
            if (!this.mergeItemStack(stack0, 3, 39, true)) {
                return;
            }
            if (stack0.stackSize <= 0) {
                stack0 = null;
            }
            this.merchantInventory.setInventorySlotContents(0, stack0);
        }

        ItemStack stack1 = this.merchantInventory.getStackInSlot(1);
        if (stack1 != null && stack1.stackSize > 0) {
            if (!this.mergeItemStack(stack1, 3, 39, true)) {
                return;
            }
            if (stack1.stackSize <= 0) {
                stack1 = null;
            }
            this.merchantInventory.setInventorySlotContents(1, stack1);
        }

        if (this.merchantInventory.getStackInSlot(0) == null && this.merchantInventory.getStackInSlot(1) == null) {
            MerchantRecipe recipe = (MerchantRecipe) trades.get(tradeIndex);
            if (recipe != null) {
                ItemStack toBuy1 = recipe.getItemToBuy();
                ItemStack toBuy2 = recipe.getSecondItemToBuy();

                if (toBuy1 != null) {
                    this.moveTradeItem(0, toBuy1);
                }
                if (toBuy2 != null) {
                    this.moveTradeItem(1, toBuy2);
                }
            }
        }
    }

    private void moveTradeItem(int merchantSlot, ItemStack requiredStack) {
        if (requiredStack == null) {
            return;
        }

        for (int i = 3; i <= 38; ++i) {
            Slot slot = (Slot) this.inventorySlots.get(i);
            ItemStack itemStack = slot.getStack();

            if (itemStack != null && areItemStacksEqual(requiredStack, itemStack)) {
                ItemStack currentMerchantItem = this.merchantInventory.getStackInSlot(merchantSlot);
                int count = (currentMerchantItem == null) ? 0 : currentMerchantItem.stackSize;
                int addition = Math.min(requiredStack.getMaxStackSize() - count, itemStack.stackSize);

                ItemStack newMerchantItem = itemStack.copy();
                int newCount = count + addition;
                itemStack.stackSize -= addition;

                if (itemStack.stackSize <= 0) {
                    slot.putStack(null);
                } else {
                    slot.onSlotChanged();
                }

                newMerchantItem.stackSize = newCount;
                this.merchantInventory.setInventorySlotContents(merchantSlot, newMerchantItem);

                if (newCount >= requiredStack.getMaxStackSize()) {
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack slotClick(int slotId, int clickedButton, int mode, EntityPlayer player) {
        if (slotId == 2) {
            Slot slot = (Slot) this.inventorySlots.get(2);
            if (slot != null && slot.getHasStack()) {
                MerchantRecipe recipe = this.merchantInventory.getCurrentRecipe();
                if (recipe != null) {
                    ((AccessorMerchantRecipe) recipe).setToolUses(((AccessorMerchantRecipe) recipe).getToolUses() + 1);
                }
            }
        }
        return super.slotClick(slotId, clickedButton, mode, player);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (this.playerInv.player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMP = (EntityPlayerMP) this.playerInv.player;
            MerchantRecipeList recipes = this.merchant.getRecipes(playerMP);
            if (recipes != null) {
                int size = recipes.size();
                boolean needsSync = (this.lastSentUses == null || this.lastSentUses.length != size);
                int[] currentUses = new int[size];
                for (int i = 0; i < size; ++i) {
                    MerchantRecipe r = (MerchantRecipe) recipes.get(i);
                    currentUses[i] = ((AccessorMerchantRecipe) r).getToolUses();
                    if (!needsSync && this.lastSentUses != null && this.lastSentUses[i] != currentUses[i]) {
                        needsSync = true;
                    }
                }

                if (needsSync) {
                    this.lastSentUses = currentUses;
                    ModNetwork.sendSyncTradeUses(playerMP, currentUses);
                }
            }
        }
    }

    private static boolean areItemStacksEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return false;
        }
        return stack1.getItem() == stack2.getItem()
            && (!stack1.getHasSubtypes() || stack1.getItemDamage() == stack2.getItemDamage())
            && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
}
