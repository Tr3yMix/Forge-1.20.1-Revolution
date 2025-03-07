package dev.tr3ymix.revolution.block.entity;

import dev.tr3ymix.revolution.registry.ModBlockEntities;
import dev.tr3ymix.revolution.inventory.ClayFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;




public class ClayFurnaceBlockEntity extends AbstractFurnaceBlockEntity {

    private final RecipeManager.CachedCheck<Container, ? extends AbstractCookingRecipe> quickCheck;

    public ClayFurnaceBlockEntity(BlockPos pPos, BlockState pState) {
        this(pPos, pState, RecipeType.SMELTING);
    }

    public ClayFurnaceBlockEntity(BlockPos pPos, BlockState pState, RecipeType<? extends AbstractCookingRecipe> pRecipeType) {
        super(ModBlockEntities.CLAY_FURNACE.get(), pPos, pState, RecipeType.SMELTING);
        this.quickCheck = RecipeManager.createCheck(pRecipeType);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("revolution.container.clay_furnace");
    }

    @Override
    protected int getBurnDuration(@NotNull ItemStack pFuel) {
        return super.getBurnDuration(pFuel) * 2;
    }



    private static int getTotalCookTime(Level pLevel, ClayFurnaceBlockEntity pBlockEntity) {
        return pBlockEntity.quickCheck.getRecipeFor(pBlockEntity, pLevel).map(AbstractCookingRecipe::getCookingTime).orElse(200) * 2;
    }



    @Override
    protected @NotNull AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory) {
        return new ClayFurnaceMenu(i, inventory, this, this.dataAccess);
    }
}
