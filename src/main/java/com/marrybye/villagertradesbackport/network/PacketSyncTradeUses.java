package com.marrybye.villagertradesbackport.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import com.marrybye.villagertradesbackport.container.ContainerVillager;
import com.marrybye.villagertradesbackport.gui.GuiVillager;
import com.marrybye.villagertradesbackport.mixins.AccessorMerchantRecipe;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;

public class PacketSyncTradeUses implements IMessage {

    private int level;
    private int xp;
    private int minXp;
    private int maxXp;
    private String professionTitle;
    private int[] uses;

    public PacketSyncTradeUses() {}

    public PacketSyncTradeUses(int level, int xp, int minXp, int maxXp, String professionTitle, int[] uses) {
        this.level = level;
        this.xp = xp;
        this.minXp = minXp;
        this.maxXp = maxXp;
        this.professionTitle = (professionTitle != null) ? professionTitle : "";
        this.uses = uses;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.level = buf.readInt();
        this.xp = buf.readInt();
        this.minXp = buf.readInt();
        this.maxXp = buf.readInt();
        this.professionTitle = ByteBufUtils.readUTF8String(buf);
        int length = buf.readInt();
        this.uses = new int[length];
        for (int i = 0; i < length; ++i) {
            this.uses[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.level);
        buf.writeInt(this.xp);
        buf.writeInt(this.minXp);
        buf.writeInt(this.maxXp);
        ByteBufUtils.writeUTF8String(buf, this.professionTitle != null ? this.professionTitle : "");
        if (this.uses == null) {
            buf.writeInt(0);
        } else {
            buf.writeInt(this.uses.length);
            for (int u : this.uses) {
                buf.writeInt(u);
            }
        }
    }

    public static PacketSyncTradeUses lastReceivedSync;

    public static void applySync(PacketSyncTradeUses message, ContainerVillager container, EntityPlayer player) {
        if (message == null || container == null) {
            return;
        }
        container.setVillagerLevel(message.level);
        container.setVillagerXp(message.xp);
        container.setMinXp(message.minXp);
        container.setMaxXp(message.maxXp);
        container.setProfessionTitle(message.professionTitle);

        if (message.uses != null && player != null) {
            IMerchant merchant = container.getMerchant();
            if (merchant != null) {
                MerchantRecipeList recipes = merchant.getRecipes(player);
                if (recipes != null) {
                    for (int i = 0; i < Math.min(recipes.size(), message.uses.length); ++i) {
                        MerchantRecipe recipe = (MerchantRecipe) recipes.get(i);
                        ((AccessorMerchantRecipe) recipe).setToolUses(message.uses[i]);
                    }
                }
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketSyncTradeUses, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketSyncTradeUses message, MessageContext ctx) {
            lastReceivedSync = message;
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            ContainerVillager container = null;
            if (player != null && player.openContainer instanceof ContainerVillager) {
                container = (ContainerVillager) player.openContainer;
            } else if (Minecraft.getMinecraft().currentScreen instanceof GuiVillager) {
                container = ((GuiVillager) Minecraft.getMinecraft().currentScreen).getContainer();
            }

            if (container != null) {
                applySync(message, container, player);
            }
            return null;
        }
    }
}
