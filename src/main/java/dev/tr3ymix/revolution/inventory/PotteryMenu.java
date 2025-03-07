package dev.tr3ymix.revolution.inventory;

import com.google.common.collect.Lists;
import dev.tr3ymix.revolution.registry.ModBlocks;
import dev.tr3ymix.revolution.registry.ModMenuTypes;
import dev.tr3ymix.revolution.item.crafting.PotteryRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PotteryMenu extends AbstractContainerMenu {

    private final ContainerLevelAccess access;
    private final DataSlot selectedRecipeIndex;
    private final Level level;
    private List<PotteryRecipe> recipes;
    private ItemStack input;
    long lastSoundTime;
    final Slot inputSlot;
    final Slot outputSlot;
    Runnable slotUpdateListener;
    public final Container container;
    final ResultContainer resultContainer;

    @SuppressWarnings("unused")
    public PotteryMenu(int i, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(i, inventory);
    }

    public PotteryMenu(int pId, Inventory pInventory) {
        this(pId, pInventory, ContainerLevelAccess.NULL);
    }



    public PotteryMenu(int pContainerId, Inventory pInventory, final ContainerLevelAccess pAccess) {
        super(MenuType.ANVIL, pContainerId);
        this.selectedRecipeIndex = DataSlot.standalone();
        this.recipes = Lists.newArrayList();
        this.input = ItemStack.EMPTY;
        this.slotUpdateListener = () -> {};
        this.container = new SimpleContainer(1){
            public void setChanged() {
                super.setChanged();
                PotteryMenu.this.slotsChanged(this);
                PotteryMenu.this.slotUpdateListener.run();
            }
        };
        this.resultContainer = new ResultContainer();
        this.access = pAccess;
        this.level = pInventory.player.level();
        this.inputSlot = this.addSlot(new Slot(this.container, 0, 20, 33));
        this.outputSlot = this.addSlot(new Slot(this.resultContainer, 1, 143, 33){
            public boolean mayPlace(@NotNull ItemStack stack) {return false;}

            @Override
            public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
                pStack.onCraftedBy(pPlayer.level(), pPlayer, pStack.getCount());
                PotteryMenu.this.resultContainer.awardUsedRecipes(pPlayer, List.of(PotteryMenu.this.inputSlot.getItem()));
                ItemStack stack = PotteryMenu.this.inputSlot.remove(1);
                if(!stack.isEmpty()) {
                    PotteryMenu.this.setupResultSlot();
                }

                pAccess.execute((level, pos) -> {
                    long time = level.getGameTime();
                    if(PotteryMenu.this.lastSoundTime != time) {
                        level.playSound(null, pos, SoundEvents.MUD_STEP, SoundSource.BLOCKS, 1.0F, 1.0F);
                        PotteryMenu.this.lastSoundTime = time;
                    }
                });
                super.onTake(pPlayer, pStack);
            }
        });

        int i;
        for(i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(pInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(i = 0; i < 9; ++i) {
            this.addSlot(new Slot(pInventory, i, 8 + i * 18, 142));
        }

        this.addDataSlot(this.selectedRecipeIndex);
    }

    public int getSelectedRecipeIndex(){
        return this.selectedRecipeIndex.get();
    }

    public List<PotteryRecipe> getRecipes() {
        return this.recipes;
    }

    public int getNumRecipes(){
        return this.recipes.size();
    }

    public boolean hasInputItem() {
        return this.inputSlot.hasItem() && !this.recipes.isEmpty();
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.POTTERY_TABLE.get());
    }

    @Override
    public boolean clickMenuButton(@NotNull Player pPlayer, int pId) {
        if(this.isValidSlotIndex(pId)){
            this.selectedRecipeIndex.set(pId);
            this.setupResultSlot();
        }
        return true;
    }

    public boolean isValidRecipeIndex(int pSlotIndex) {
        return pSlotIndex >= 0 && pSlotIndex < this.recipes.size();
    }

    @Override
    public void slotsChanged(@NotNull Container pContainer) {
        ItemStack stack = this.inputSlot.getItem();
        if(!stack.is(this.input.getItem())) {
            this.input = stack.copy();
            this.setupRecipeList(pContainer, stack);
        }
    }

    private void setupRecipeList(Container pContainer, ItemStack pStack) {
        this.recipes.clear();
        this.selectedRecipeIndex.set(-1);
        this.outputSlot.set(ItemStack.EMPTY);
        if (!pStack.isEmpty()) {
            this.recipes = this.level.getRecipeManager().getRecipesFor(PotteryRecipe.Type.INSTANCE, pContainer, this.level);
        }

    }

    void setupResultSlot(){
        if (!this.recipes.isEmpty() && this.isValidRecipeIndex(this.selectedRecipeIndex.get())) {
            PotteryRecipe recipe = this.recipes.get(this.selectedRecipeIndex.get());
            ItemStack stack = recipe.assemble(this.container, this.level.registryAccess());
            if (stack.isItemEnabled(this.level.enabledFeatures())) {
                this.resultContainer.setRecipeUsed(recipe);
                this.outputSlot.set(stack);
            } else {
                this.outputSlot.set(ItemStack.EMPTY);
            }
        } else {
            this.outputSlot.set(ItemStack.EMPTY);
        }

        this.broadcastChanges();
    }

    @Override
    public @NotNull MenuType<?> getType() {
        return ModMenuTypes.POTTERY_MENU.get();
    }

    public void registerUpdateListener(Runnable pListener){
        this.slotUpdateListener = pListener;
    }

    @Override
    public boolean canTakeItemForPickAll(@NotNull ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultContainer && super.canTakeItemForPickAll(pStack, pSlot);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        ItemStack emptyStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if(slot.hasItem()) {
            ItemStack stack = slot.getItem();
            Item item = stack.getItem();
            emptyStack = stack.copy();

            if(i == 1){
                item.onCraftedBy(stack, player.level(), player);
                if(!this.moveItemStackTo(stack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, emptyStack);
            } else if (i == 0) {
                if(!this.moveItemStackTo(stack, 2, 38, false)) {
                    return ItemStack.EMPTY;
                }
            }else if(this.level.getRecipeManager().getRecipeFor(PotteryRecipe.Type.INSTANCE, new SimpleContainer(stack), this.level).isPresent()) {
                if(!this.moveItemStackTo(stack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            }else if(i >= 2 && i < 29){
                if(!this.moveItemStackTo(stack, 29, 38, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i >= 29 && i < 38 && !this.moveItemStackTo(stack, 2, 29, false)) {
                return ItemStack.EMPTY;
            }

            if(stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            }

            slot.setChanged();
            if(stack.getCount() == emptyStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
            this.broadcastChanges();
        }

        return emptyStack;
    }

    @Override
    public void removed(@NotNull Player pPlayer) {
        super.removed(pPlayer);
        this.resultContainer.removeItemNoUpdate(1);
        this.access.execute((level, pos) -> this.clearContainer(pPlayer, this.container));
    }
}
