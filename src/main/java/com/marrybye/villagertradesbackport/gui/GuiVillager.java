package com.marrybye.villagertradesbackport.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.IMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.marrybye.villagertradesbackport.compat.VillageNamesCompat;
import com.marrybye.villagertradesbackport.container.ContainerVillager;
import com.marrybye.villagertradesbackport.network.ModNetwork;

public class GuiVillager extends GuiMerchant {

    private static final ResourceLocation TEXTURE = new ResourceLocation(
        "villagertradesbackport",
        "textures/gui/villager.png");

    private static final String[] LEVEL_KEYS = new String[] { "container.villagertrades.novice",
        "container.villagertrades.apprentice", "container.villagertrades.journeyman", "container.villagertrades.expert",
        "container.villagertrades.master" };

    private final ContainerVillager container;
    private final IMerchant theMerchant;
    private final String titleText;
    private int selectedTradeIndex = 0;
    private int scrollOffset = 0;
    private boolean clickedOnScroll = false;
    private final TradeButton[] tradeButtons = new TradeButton[7];

    public GuiVillager(ContainerVillager container, IMerchant merchant, String title) {
        super(Minecraft.getMinecraft().thePlayer.inventory, merchant, Minecraft.getMinecraft().theWorld, title);
        this.container = container;
        this.theMerchant = merchant;
        this.titleText = title;
        this.inventorySlots = container;
        this.xSize = 276;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        this.mc.thePlayer.openContainer = this.inventorySlots;
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.clear();

        int startX = this.guiLeft + 5;
        int k = this.guiTop + 16 + 2;

        for (int id = 0; id < 7; ++id) {
            TradeButton btn = new TradeButton(id, startX, k);
            this.tradeButtons[id] = btn;
            this.buttonList.add(btn);
            k += 20;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        int level = VillageNamesCompat.getVillagerLevel(this.theMerchant, trades);
        String levelName = StatCollector.translateToLocal(LEVEL_KEYS[Math.max(0, Math.min(4, level - 1))]);

        String displayName = this.titleText;
        if (displayName == null || displayName.trim()
            .isEmpty()) {
            displayName = StatCollector.translateToLocal("entity.Villager.name");
        }

        String fullTitle = displayName + " - " + levelName;
        int j = this.fontRendererObj.getStringWidth(fullTitle);
        int k = 49 + (this.xSize / 2) - (j / 2);
        this.fontRendererObj.drawString(fullTitle, k, 6, 0x404040);

        // Player Inventory title
        String invName = this.container.getPlayerInv()
            .hasCustomInventoryName()
                ? this.container.getPlayerInv()
                    .getInventoryName()
                : StatCollector.translateToLocal(
                    this.container.getPlayerInv()
                        .getInventoryName());
        this.fontRendererObj.drawString(invName, 107, this.ySize - 94, 0x404040);

        // Trades panel title
        String tradesLabel = StatCollector.translateToLocal("container.villagertrades.trades");
        int l = this.fontRendererObj.getStringWidth(tradesLabel);
        this.fontRendererObj.drawString(tradesLabel, 5 + 48 - (l / 2), 6, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager()
            .bindTexture(TEXTURE);
        int x = this.guiLeft;
        int y = this.guiTop;

        // Render main 276x166 GUI background
        drawCustomTexturedRect(x, y, 0.0F, 0.0F, this.xSize, this.ySize, 512.0F, 256.0F, this.zLevel);

        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        if (trades != null && !trades.isEmpty()) {
            if (this.selectedTradeIndex >= 0 && this.selectedTradeIndex < trades.size()) {
                MerchantRecipe trade = (MerchantRecipe) trades.get(this.selectedTradeIndex);
                if (trade.isRecipeDisabled()) {
                    this.mc.getTextureManager()
                        .bindTexture(TEXTURE);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    // Big red cross over right-hand trade arrow
                    drawCustomTexturedRect(
                        this.guiLeft + 182,
                        this.guiTop + 35,
                        311.0F,
                        0.0F,
                        28,
                        21,
                        512.0F,
                        256.0F,
                        this.zLevel);
                }
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // Draw left panel trades list
        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        if (trades != null && !trades.isEmpty()) {
            int startX = this.guiLeft;
            int startY = this.guiTop;
            int listTop = startY + 16 + 1;
            int cost1X = startX + 5 + 5;

            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            this.mc.getTextureManager()
                .bindTexture(TEXTURE);
            this.renderScrollBar(startX, startY, trades);

            int tradeDisplayCount = 0;
            for (int t = 0; t < trades.size(); ++t) {
                if (canScroll(trades.size()) && (t < this.scrollOffset || t >= 7 + this.scrollOffset)) {
                    // Out of scroll view
                } else {
                    MerchantRecipe trade = (MerchantRecipe) trades.get(t);
                    ItemStack cost1 = trade.getItemToBuy();
                    ItemStack cost2 = trade.getSecondItemToBuy();
                    ItemStack result = trade.getItemToSell();

                    int rowY = listTop + tradeDisplayCount * 20;
                    int itemY = rowY + 2;

                    itemRender.zLevel = 100.0F;

                    // Render input 1
                    if (cost1 != null) {
                        this.renderTradeItem(cost1, cost1X, itemY);
                    }

                    // Render input 2
                    if (cost2 != null) {
                        this.renderTradeItem(cost2, startX + 5 + 35, itemY);
                    }

                    // Render trade arrow
                    this.renderArrow(trade, startX, rowY + 5);

                    // Render result item
                    if (result != null) {
                        this.renderTradeItem(result, startX + 5 + 68, itemY);
                    }

                    itemRender.zLevel = 0.0F;
                    tradeDisplayCount++;
                }
            }

            // Update button visibility and check trade button tooltips
            TradeButton hoveredTradeButton = null;
            for (TradeButton btn : this.tradeButtons) {
                if (btn != null) {
                    btn.visible = (btn.id + this.scrollOffset) < trades.size();
                    if (btn.visible && btn.func_146115_a()) {
                        hoveredTradeButton = btn;
                    }
                }
            }

            GL11.glPopMatrix();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_LIGHTING);
            RenderHelper.enableStandardItemLighting();

            // Render tooltip for hovered item in trades list
            if (hoveredTradeButton != null) {
                hoveredTradeButton.renderToolTip(mouseX, mouseY, trades);
            }
        }
    }

    private void renderScrollBar(int x, int y, MerchantRecipeList trades) {
        int i = trades.size() + 1 - 7;
        if (i > 1) {
            int j = 139 - (27 + (i - 1) * 139 / i);
            int k = 1 + j / i + 139 / i;
            int i1 = Math.min(113, this.scrollOffset * k);
            if (this.scrollOffset == i - 1) {
                i1 = 113;
            }
            drawCustomTexturedRect(x + 94, y + 18 + i1, 0.0F, 199.0F, 6, 27, 512.0F, 256.0F, this.zLevel);
        } else {
            drawCustomTexturedRect(x + 94, y + 18, 6.0F, 199.0F, 6, 27, 512.0F, 256.0F, this.zLevel);
        }
    }

    private void renderTradeItem(ItemStack stack, int x, int y) {
        if (stack != null) {
            itemRender.renderItemAndEffectIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack, x, y);
            itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, this.mc.getTextureManager(), stack, x, y);
        }
    }

    private void renderArrow(MerchantRecipe trade, int x, int y) {
        GL11.glEnable(GL11.GL_BLEND);
        this.mc.getTextureManager()
            .bindTexture(TEXTURE);
        if (trade.isRecipeDisabled()) {
            drawCustomTexturedRect(x + 5 + 35 + 20, y + 2, 25.0F, 171.0F, 10, 9, 512.0F, 256.0F, this.zLevel);
        } else {
            drawCustomTexturedRect(x + 5 + 35 + 20, y + 2, 15.0F, 171.0F, 10, 9, 512.0F, 256.0F, this.zLevel);
        }
    }

    private boolean canScroll(int size) {
        return size > 7;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.enabled && button instanceof TradeButton) {
            this.selectedTradeIndex = button.id + this.scrollOffset;
            this.postButtonClick();
        }
    }

    private void postButtonClick() {
        this.container.setCurrentRecipeIndex(this.selectedTradeIndex);
        this.container.moveAroundItems(this.selectedTradeIndex);
        ModNetwork.sendSelectTrade(this.selectedTradeIndex);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        this.clickedOnScroll = false;
        int i = this.guiLeft;
        int j = this.guiTop;

        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        if (trades != null && canScroll(trades.size())) {
            if (mouseX >= (i + 94) && mouseX <= (i + 94 + 6) && mouseY >= j + 18 && mouseY <= (j + 18 + 139)) {
                this.clickedOnScroll = true;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int delta = Mouse.getEventDWheel();
        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        if (trades != null && delta != 0 && canScroll(trades.size())) {
            delta = Integer.signum(delta);
            int maxOffset = trades.size() - 7;
            this.scrollOffset -= delta;
            this.scrollOffset = MathHelper.clamp_int(this.scrollOffset, 0, maxOffset);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        MerchantRecipeList trades = this.theMerchant.getRecipes(this.mc.thePlayer);
        if (trades != null && this.clickedOnScroll) {
            int j = this.guiTop + 18;
            int k = j + 139;
            int maxOffset = trades.size() - 7;
            float f = ((float) mouseY - (float) j - 13.5F) / (((float) k - (float) j) - 27.0F);
            f = f * (float) maxOffset + 0.5F;
            this.scrollOffset = MathHelper.clamp_int((int) f, 0, maxOffset);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    public void updateScreen() {
        if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
            this.mc.thePlayer.closeScreen();
        }
    }

    public static void drawCustomTexturedRect(int x, int y, float u, float v, int width, int height, float texWidth,
        float texHeight, double zLevel) {
        float minU = u / texWidth;
        float maxU = (u + width) / texWidth;
        float minV = v / texHeight;
        float maxV = (v + height) / texHeight;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double) x, (double) (y + height), zLevel, (double) minU, (double) maxV);
        tessellator.addVertexWithUV((double) (x + width), (double) (y + height), zLevel, (double) maxU, (double) maxV);
        tessellator.addVertexWithUV((double) (x + width), (double) y, zLevel, (double) maxU, (double) minV);
        tessellator.addVertexWithUV((double) x, (double) y, zLevel, (double) minU, (double) minV);
        tessellator.draw();
    }

    public class TradeButton extends GuiButton {

        public TradeButton(int id, int x, int y) {
            super(id, x, y, 88, 20, "");
            this.visible = false;
        }

        @Override
        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
                    && mouseX < this.xPosition + this.width
                    && mouseY < this.yPosition + this.height;

                int tradeIndex = this.id + scrollOffset;
                boolean isSelected = (tradeIndex == selectedTradeIndex);

                mc.getTextureManager()
                    .bindTexture(TEXTURE);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                if (isSelected) {
                    // Selected trade frame from villager.png
                    drawCustomTexturedRect(
                        this.xPosition,
                        this.yPosition,
                        279.0F,
                        73.0F,
                        88,
                        20,
                        512.0F,
                        256.0F,
                        zLevel);
                } else if (this.field_146123_n) {
                    // Hover highlight
                    drawGradientRect(
                        this.xPosition,
                        this.yPosition,
                        this.xPosition + this.width,
                        this.yPosition + this.height,
                        0x40FFFFFF,
                        0x40FFFFFF);
                }
            }
        }

        public void renderToolTip(int mouseX, int mouseY, MerchantRecipeList trades) {
            int tradeIndex = this.id + scrollOffset;
            if (this.field_146123_n && trades != null && tradeIndex < trades.size()) {
                MerchantRecipe trade = (MerchantRecipe) trades.get(tradeIndex);
                if (mouseX >= this.xPosition + 5 && mouseX <= this.xPosition + 25) {
                    if (trade.getItemToBuy() != null) {
                        GuiVillager.this.renderToolTip(trade.getItemToBuy(), mouseX, mouseY);
                    }
                } else if (mouseX >= this.xPosition + 35 && mouseX <= this.xPosition + 55) {
                    if (trade.getSecondItemToBuy() != null) {
                        GuiVillager.this.renderToolTip(trade.getSecondItemToBuy(), mouseX, mouseY);
                    }
                } else if (mouseX >= this.xPosition + 68 && mouseX <= this.xPosition + 88) {
                    if (trade.getItemToSell() != null) {
                        GuiVillager.this.renderToolTip(trade.getItemToSell(), mouseX, mouseY);
                    }
                }
            }
        }
    }
}
