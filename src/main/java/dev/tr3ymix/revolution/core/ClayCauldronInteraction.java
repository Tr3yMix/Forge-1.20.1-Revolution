package dev.tr3ymix.revolution.core;

import dev.tr3ymix.revolution.block.TerracottaLayeredCauldronBlock;
import dev.tr3ymix.revolution.registry.ModBlocks;
import it.unimi.dsi.fastutil.objects.AbstractObject2ObjectFunction;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;
import java.util.function.Predicate;

public interface ClayCauldronInteraction {
    Map<Item, ClayCauldronInteraction> EMPTY = newInteractionMap();
    Map<Item, ClayCauldronInteraction> WATER = newInteractionMap();
    Map<Item, ClayCauldronInteraction> POWDER_SNOW = newInteractionMap();

    ClayCauldronInteraction FILL_WATER = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyBucket(pLevel, pPos, pPlayer, pHand, pStack,
            ModBlocks.WATER_TERRACOTTA_CAULDRON.get().defaultBlockState()
                    .setValue(TerracottaLayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);

    ClayCauldronInteraction FILL_POWDER_SNOW = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    ModBlocks.POWDER_SNOW_TERRACOTTA_CAULDRON.get().defaultBlockState()
                            .setValue(TerracottaLayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);

    ClayCauldronInteraction SHULKER_BOX = (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
        Block block = Block.byItem(pStack.getItem());
        if(!(block instanceof ShulkerBoxBlock)) {
            return InteractionResult.PASS;
        } else {
            if(!pLevel.isClientSide) {
                ItemStack stack = new ItemStack(Blocks.SHULKER_BOX);
                if(pStack.hasTag()){
                    assert pStack.getTag() != null;
                    stack.setTag(pStack.getTag().copy());
                }

                pPlayer.setItemInHand(pHand, stack);
                pPlayer.awardStat(Stats.CLEAN_SHULKER_BOX);
                TerracottaLayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
            }
            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
    };

    ClayCauldronInteraction BANNER = (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
        if(BannerBlockEntity.getPatternCount(pStack) <= 0){
            return InteractionResult.PASS;
        } else {
            if(!pLevel.isClientSide){
                ItemStack stack = pStack.copyWithCount(1);
                BannerBlockEntity.removeLastPattern(stack);

                if(!pPlayer.getAbilities().instabuild){
                    pStack.shrink(1);
                }

                if(pStack.isEmpty()){
                    pPlayer.setItemInHand(pHand, stack);
                } else if (pPlayer.getInventory().add(stack)) {
                    pPlayer.inventoryMenu.sendAllDataToRemote();
                }else{
                    pPlayer.drop(stack, false);
                }

                pPlayer.awardStat(Stats.CLEAN_BANNER);
                TerracottaLayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
    };

    ClayCauldronInteraction DYED_ITEM = (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
        Item item = pStack.getItem();
        if(!(item instanceof DyeableLeatherItem dyeableLeatherItem)){
            return InteractionResult.PASS;
        } else if (!dyeableLeatherItem.hasCustomColor(pStack)){
            return InteractionResult.PASS;
        } else {
            if(!pLevel.isClientSide){
                dyeableLeatherItem.clearColor(pStack);
                pPlayer.awardStat(Stats.CLEAN_ARMOR);
                TerracottaLayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
    };


    static Object2ObjectOpenHashMap<Item, ClayCauldronInteraction> newInteractionMap() {
        return Util.make(new Object2ObjectOpenHashMap<>(), AbstractObject2ObjectFunction::defaultReturnValue);
    }

    InteractionResult interact(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, ItemStack pStack);

    static void bootStrap(){

        addDefaultInteractions(EMPTY);
        EMPTY.put(Items.POTION, (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
            if(PotionUtils.getPotion(pStack) != Potions.WATER){
                return InteractionResult.PASS;
            } else {
                if(!pLevel.isClientSide){
                    Item item = pStack.getItem();
                    pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Items.GLASS_BOTTLE)));
                    pPlayer.awardStat(Stats.USE_CAULDRON);
                    pPlayer.awardStat(Stats.ITEM_USED.get(item));
                    pLevel.setBlockAndUpdate(pPos, ModBlocks.WATER_TERRACOTTA_CAULDRON.get().defaultBlockState());
                    pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.gameEvent(null, GameEvent.FLUID_PLACE, pPos);
                }

                return InteractionResult.sidedSuccess(pLevel.isClientSide());
            }
        });
        addDefaultInteractions(WATER);
        WATER.put(Items.BUCKET, (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                new ItemStack(Items.WATER_BUCKET), (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL));
        WATER.put(Items.GLASS_BOTTLE, (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
            if(!pLevel.isClientSide){
                Item item = pStack.getItem();
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
                pPlayer.awardStat(Stats.USE_CAULDRON);
                pPlayer.awardStat(Stats.ITEM_USED.get(item));
                TerracottaLayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
                pLevel.playSound(null, pPos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                pLevel.gameEvent(null, GameEvent.FLUID_PICKUP, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        });
        WATER.put(Items.POTION, (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
            if(pState.getValue(TerracottaLayeredCauldronBlock.LEVEL) != 3 && PotionUtils.getPotion(pStack) == Potions.WATER){
                if(!pLevel.isClientSide){
                    pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Items.GLASS_BOTTLE)));
                    pPlayer.awardStat(Stats.USE_CAULDRON);
                    pPlayer.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
                    pLevel.setBlockAndUpdate(pPos, pState.cycle(TerracottaLayeredCauldronBlock.LEVEL));
                    pLevel.playSound(null, pPos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pLevel.gameEvent(null, GameEvent.FLUID_PLACE, pPos);
                }

                return InteractionResult.sidedSuccess(pLevel.isClientSide());
            } else {
                return InteractionResult.PASS;
            }
        });

        WATER.put(Items.LEATHER_BOOTS, DYED_ITEM);
        WATER.put(Items.LEATHER_LEGGINGS, DYED_ITEM);
        WATER.put(Items.LEATHER_CHESTPLATE, DYED_ITEM);
        WATER.put(Items.LEATHER_HELMET, DYED_ITEM);
        WATER.put(Items.LEATHER_HORSE_ARMOR, DYED_ITEM);
        WATER.put(Items.WHITE_BANNER, BANNER);
        WATER.put(Items.GRAY_BANNER, BANNER);
        WATER.put(Items.BLACK_BANNER, BANNER);
        WATER.put(Items.BLUE_BANNER, BANNER);
        WATER.put(Items.BROWN_BANNER, BANNER);
        WATER.put(Items.CYAN_BANNER, BANNER);
        WATER.put(Items.GREEN_BANNER, BANNER);
        WATER.put(Items.LIGHT_BLUE_BANNER, BANNER);
        WATER.put(Items.LIGHT_GRAY_BANNER, BANNER);
        WATER.put(Items.LIME_BANNER, BANNER);
        WATER.put(Items.MAGENTA_BANNER, BANNER);
        WATER.put(Items.ORANGE_BANNER, BANNER);
        WATER.put(Items.PINK_BANNER, BANNER);
        WATER.put(Items.PURPLE_BANNER, BANNER);
        WATER.put(Items.RED_BANNER, BANNER);
        WATER.put(Items.YELLOW_BANNER, BANNER);
        WATER.put(Items.WHITE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.GRAY_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BLACK_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BLUE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.BROWN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.CYAN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.GREEN_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIGHT_BLUE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIGHT_GRAY_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.LIME_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.MAGENTA_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.ORANGE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.PINK_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.PURPLE_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.RED_SHULKER_BOX, SHULKER_BOX);
        WATER.put(Items.YELLOW_SHULKER_BOX, SHULKER_BOX);

        POWDER_SNOW.put(Items.BUCKET, (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack, new ItemStack(Items.POWDER_SNOW_BUCKET),
                        (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL_POWDER_SNOW));

        addDefaultInteractions(POWDER_SNOW);
    }

    static void addDefaultInteractions(Map<Item, ClayCauldronInteraction> pInteractionsMap) {
        pInteractionsMap.put(Items.WATER_BUCKET, FILL_WATER);
        pInteractionsMap.put(Items.POWDER_SNOW_BUCKET, FILL_POWDER_SNOW);
    }

    static InteractionResult fillBucket(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer,
                                        InteractionHand pHand, ItemStack pEmptyStack, ItemStack pFilledStack,
                                        Predicate<BlockState> pStatePredicate, SoundEvent pFillSound) {
        if(!pStatePredicate.test(pState)){
            return InteractionResult.PASS;
        } else {
            if(!pLevel.isClientSide){
                Item item = pEmptyStack.getItem();
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pEmptyStack, pPlayer, pFilledStack));
                pPlayer.awardStat(Stats.USE_CAULDRON);
                pPlayer.awardStat(Stats.ITEM_USED.get(item));
                pLevel.setBlockAndUpdate(pPos, ModBlocks.TERRACOTTA_CAULDRON.get().defaultBlockState());
                pLevel.playSound(null, pPos, pFillSound, SoundSource.BLOCKS, 1.0f, 1.0f);
                pLevel.gameEvent(null, GameEvent.FLUID_PICKUP, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide());
        }
    }

    static InteractionResult emptyBucket(Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                         ItemStack pStack, BlockState pState, SoundEvent pEmptySound) {
        if(!pLevel.isClientSide) {
            Item item = pStack.getItem();
            pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Items.BUCKET)));
            pPlayer.awardStat(Stats.FILL_CAULDRON);
            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            pLevel.setBlockAndUpdate(pPos, pState);
            pLevel.playSound(null, pPos, pEmptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent(null, GameEvent.FLUID_PLACE, pPos);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
