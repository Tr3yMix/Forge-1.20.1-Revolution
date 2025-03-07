package dev.tr3ymix.revolution.inventory;

import dev.tr3ymix.revolution.registry.ModMenuTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeType;

public class ClayFurnaceMenu extends AbstractFurnaceMenu {
    @SuppressWarnings("unused")
    public ClayFurnaceMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory);
    }
    public ClayFurnaceMenu(int pContainerId, Inventory pInv) {
        super(ModMenuTypes.CLAY_FURNACE_MENU.get(), RecipeType.SMELTING,RecipeBookType.FURNACE, pContainerId, pInv);
    }

    public ClayFurnaceMenu(int pContainerId, Inventory pInv, Container pContainer, ContainerData pContainerData) {
        super(ModMenuTypes.CLAY_FURNACE_MENU.get(), RecipeType.SMELTING, RecipeBookType.FURNACE, pContainerId, pInv, pContainer, pContainerData);
    }


}
