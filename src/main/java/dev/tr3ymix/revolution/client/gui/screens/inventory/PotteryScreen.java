package dev.tr3ymix.revolution.client.gui.screens.inventory;

import dev.tr3ymix.revolution.inventory.PotteryMenu;
import dev.tr3ymix.revolution.item.crafting.PotteryRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PotteryScreen extends AbstractContainerScreen<PotteryMenu> {

    private static final ResourceLocation BG_LOCATION = new ResourceLocation("textures/gui/container/stonecutter.png");
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;
    private boolean displayRecipes;

    public PotteryScreen(PotteryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        pMenu.registerUpdateListener(this::containerChanged);
        --this.titleLabelY;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int x, int y) {
        this.renderBackground(guiGraphics);
        int leftPos = this.leftPos;
        int topPos = this.topPos;
        guiGraphics.blit(BG_LOCATION, leftPos, topPos, 0, 0, this.imageWidth, this.imageHeight);
        int offset = (int) (41.0F * this.scrollOffs);
        guiGraphics.blit(BG_LOCATION, leftPos + 119, topPos + 15 + offset, 176 + (this.isScrollBarActive() ? 0 : 12),
                0, 12, 15);
        this.renderButtons(guiGraphics, x, y, this.leftPos + 52, this.topPos + 14, this.startIndex + 12);
        this.renderRecipes(guiGraphics, this.leftPos + 52, this.topPos + 14, this.startIndex + 12);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY) {
        super.renderTooltip(pGuiGraphics, pX, pY);

        if (this.displayRecipes) {
            int startX = this.leftPos + 52;
            int startY = this.topPos + 14;
            int maxIndex = this.startIndex + 12;
            List<PotteryRecipe> recipes = this.menu.getRecipes();

            for (int recipeIndex = this.startIndex; recipeIndex < maxIndex && recipeIndex < this.menu.getNumRecipes(); ++recipeIndex) {
                int relativeIndex = recipeIndex - this.startIndex;
                int recipeX = startX + (relativeIndex % 4) * 16;
                int recipeY = startY + (relativeIndex / 4) * 18 + 2;

                if (pX >= recipeX && pX < recipeX + 16 && pY >= recipeY && pY < recipeY + 18) {
                    assert this.minecraft != null;
                    assert this.minecraft.level != null;
                    pGuiGraphics.renderTooltip(this.font, recipes.get(recipeIndex).getResultItem(this.minecraft.level.registryAccess()), pX, pY);
                }
            }
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex) {
        for (int recipeIndex = this.startIndex; recipeIndex < pLastVisibleElementIndex && recipeIndex < this.menu.getNumRecipes(); ++recipeIndex) {
            int relativeIndex = recipeIndex - this.startIndex;
            int buttonX = pX + (relativeIndex % 4) * 16;
            int rowIndex = relativeIndex / 4;
            int buttonY = pY + rowIndex * 18 + 2;
            int textureOffsetY = this.imageHeight;

            if (recipeIndex == this.menu.getSelectedRecipeIndex()) {
                textureOffsetY += 18;
            } else if (pMouseX >= buttonX && pMouseY >= buttonY && pMouseX < buttonX + 16 && pMouseY < buttonY + 18) {
                textureOffsetY += 36;
            }

            guiGraphics.blit(BG_LOCATION, buttonX, buttonY - 1, 0, textureOffsetY, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int pX, int pY, int pStartIndex) {
        List<PotteryRecipe> recipes = this.menu.getRecipes();

        for (int recipeIndex = this.startIndex; recipeIndex < pStartIndex && recipeIndex < this.menu.getNumRecipes(); ++recipeIndex) {
            int relativeIndex = recipeIndex - this.startIndex;
            int itemX = pX + (relativeIndex % 4) * 16;
            int rowIndex = relativeIndex / 4;
            int itemY = pY + rowIndex * 18 + 2;

            assert this.minecraft != null;
            assert this.minecraft.level != null;
            guiGraphics.renderItem(recipes.get(recipeIndex).getResultItem(this.minecraft.level.registryAccess()), itemX, itemY);
        }
    }


    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;

        if (this.displayRecipes) {
            int startX = this.leftPos + 52;
            int startY = this.topPos + 14;
            int maxIndex = this.startIndex + 12;

            for (int recipeIndex = this.startIndex; recipeIndex < maxIndex; ++recipeIndex) {
                int relativeIndex = recipeIndex - this.startIndex;
                double offsetX = pMouseX - (double) (startX + (relativeIndex % 4) * 16);
                double offsetY = pMouseY - (double) (startY + (relativeIndex / 4) * 18);

                if (offsetX >= 0.0 && offsetY >= 0.0 && offsetX < 16.0 && offsetY < 18.0) {
                    assert this.minecraft != null;
                    assert this.minecraft.player != null;
                    if (this.menu.clickMenuButton(this.minecraft.player, recipeIndex)) {

                        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                        assert this.minecraft.gameMode != null;
                        this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, recipeIndex);
                        return true;
                    }
                }
            }

            int scrollbarX = this.leftPos + 119;
            int scrollbarY = this.topPos + 9;

            if (pMouseX >= (double) scrollbarX && pMouseX < (double) (scrollbarX + 12) &&
                    pMouseY >= (double) scrollbarY && pMouseY < (double) (scrollbarY + 54)) {
                this.scrolling = true;
            }
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int scrollStartY = this.topPos + 14;
            int scrollEndY = scrollStartY + 54;

            this.scrollOffs = ((float) pMouseY - (float) scrollStartY - 7.5F) / ((float) (scrollEndY - scrollStartY) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int offscreenRows = this.getOffscreenRows();
            float scrollAmount = (float) pDelta / (float) offscreenRows;

            this.scrollOffs = Mth.clamp(this.scrollOffs - scrollAmount, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) offscreenRows) + 0.5) * 4;
        }

        return true;
    }

    private boolean isScrollBarActive() {
        return this.displayRecipes && this.menu.getNumRecipes() > 12;
    }

    protected int getOffscreenRows(){return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;}

    private void containerChanged() {
        this.displayRecipes = this.menu.hasInputItem();
        if (!this.displayRecipes) {
            this.scrollOffs = 0.0F;
            this.startIndex = 0;
        }
    }
}
