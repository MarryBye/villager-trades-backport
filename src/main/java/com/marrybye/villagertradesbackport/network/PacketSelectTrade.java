package com.marrybye.villagertradesbackport.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerMerchant;

import com.marrybye.villagertradesbackport.container.ContainerVillager;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PacketSelectTrade implements IMessage {

    private int tradeIndex;

    public PacketSelectTrade() {}

    public PacketSelectTrade(int tradeIndex) {
        this.tradeIndex = tradeIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tradeIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.tradeIndex);
    }

    public static class Handler implements IMessageHandler<PacketSelectTrade, IMessage> {

        @Override
        public IMessage onMessage(PacketSelectTrade message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if (player != null && player.openContainer instanceof ContainerMerchant) {
                ContainerMerchant container = (ContainerMerchant) player.openContainer;
                if (message.tradeIndex >= 0) {
                    container.setCurrentRecipeIndex(message.tradeIndex);
                    if (container instanceof ContainerVillager) {
                        ((ContainerVillager) container).moveAroundItems(message.tradeIndex);
                    }
                } else if (container instanceof ContainerVillager) {
                    ((ContainerVillager) container).sendSyncPacket(player);
                }
            }
            return null;
        }
    }
}
