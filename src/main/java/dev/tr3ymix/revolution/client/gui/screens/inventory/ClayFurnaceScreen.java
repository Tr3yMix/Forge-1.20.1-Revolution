package dev.tr3ymix.revolution.client.gui.screens.inventory;

import dev.tr3ymix.revolution.inventory.ClayFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClayFurnaceScreen extends AbstractFurnaceScreen<ClayFurnaceMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");

    public ClayFurnaceScreen(ClayFurnaceMenu menu, Inventory inventory, Component title) {
        super(menu, new SmeltingRecipeBookComponent(), inventory, title, TEXTURE);
    }
}
