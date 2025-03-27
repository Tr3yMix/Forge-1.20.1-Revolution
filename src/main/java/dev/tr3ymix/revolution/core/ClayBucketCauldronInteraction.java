package dev.tr3ymix.revolution.core;

import dev.tr3ymix.revolution.block.TerracottaLayeredCauldronBlock;
import dev.tr3ymix.revolution.registry.ModBlocks;
import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.Map;


public interface ClayBucketCauldronInteraction {

    ClayCauldronInteraction FILL_WATER_CLAY = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyClayBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    ModBlocks.WATER_TERRACOTTA_CAULDRON.get().defaultBlockState()
                            .setValue(TerracottaLayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);

    ClayCauldronInteraction FILL_POWDER_SNOW_CLAY = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyClayBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    ModBlocks.POWDER_SNOW_TERRACOTTA_CAULDRON.get().defaultBlockState()
                            .setValue(TerracottaLayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);


    CauldronInteraction FILL_WATER = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyClayBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    Blocks.WATER_CAULDRON.defaultBlockState()
                            .setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);

    CauldronInteraction FILL_POWDER_SNOW = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyClayBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    Blocks.POWDER_SNOW_CAULDRON.defaultBlockState()
                            .setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY_POWDER_SNOW);

    CauldronInteraction FILL_LAVA = (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
            emptyClayBucket(pLevel, pPos, pPlayer, pHand, pStack,
                    Blocks.LAVA_CAULDRON.defaultBlockState(), SoundEvents.BUCKET_EMPTY_LAVA);

    static void bootStrap(){
        clayCauldronBootStrap();
        vanillaCauldronBootStrap();
    }

    static void clayCauldronBootStrap(){

        addClayCauldronDefaultClayInteractions(ClayCauldronInteraction.EMPTY);
        addClayCauldronDefaultClayInteractions(ClayCauldronInteraction.WATER);
        addClayCauldronDefaultClayInteractions(ClayCauldronInteraction.POWDER_SNOW);

        ClayCauldronInteraction.WATER.put(ModItems.CLAY_BUCKET.get(), (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                ClayCauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                        new ItemStack(ModItems.CLAY_WATER_BUCKET.get()), (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL));

        ClayCauldronInteraction.POWDER_SNOW.put(ModItems.CLAY_BUCKET.get(), (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                ClayCauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack, new ItemStack(ModItems.CLAY_POWDER_SNOW_BUCKET.get()),
                        (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL_POWDER_SNOW));

        ClayCauldronInteraction.WATER.put(Items.DIRT, (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
            if(!pLevel.isClientSide){
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Items.MUD)));
                pPlayer.awardStat(Stats.USE_CAULDRON);
                pLevel.setBlockAndUpdate(pPos, pState.cycle(TerracottaLayeredCauldronBlock.LEVEL));
                pLevel.playSound(null, pPos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                TerracottaLayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        });
    }

    static void vanillaCauldronBootStrap(){

        addVanillaCauldronDefaultClayInteractions(CauldronInteraction.EMPTY);
        addVanillaCauldronDefaultClayInteractions(CauldronInteraction.WATER);
        addVanillaCauldronDefaultClayInteractions(CauldronInteraction.POWDER_SNOW);
        addVanillaCauldronDefaultClayInteractions(CauldronInteraction.LAVA);

        CauldronInteraction.WATER.put(ModItems.CLAY_BUCKET.get(), (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                        new ItemStack(ModItems.CLAY_WATER_BUCKET.get()), (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL));

        CauldronInteraction.POWDER_SNOW.put(ModItems.CLAY_BUCKET.get(), (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack, new ItemStack(ModItems.CLAY_POWDER_SNOW_BUCKET.get()),
                        (blockState -> blockState.getValue(TerracottaLayeredCauldronBlock.LEVEL) == 3),
                        SoundEvents.BUCKET_FILL_POWDER_SNOW));

        CauldronInteraction.WATER.put(Items.DIRT, (pState, pLevel, pPos, pPlayer, pHand, pStack) -> {
            if(!pLevel.isClientSide){
                pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, new ItemStack(Items.MUD)));
                pPlayer.awardStat(Stats.USE_CAULDRON);
                pLevel.setBlockAndUpdate(pPos, pState.cycle(LayeredCauldronBlock.LEVEL));
                pLevel.playSound(null, pPos, SoundEvents.MUD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                LayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
            }

            return InteractionResult.sidedSuccess(pLevel.isClientSide);
        });

        CauldronInteraction.LAVA.put(ModItems.CLAY_BUCKET.get(), (pState, pLevel, pPos, pPlayer, pHand, pStack) ->
                CauldronInteraction.fillBucket(pState, pLevel, pPos, pPlayer, pHand, pStack,
                        new ItemStack(ModItems.CLAY_LAVA_BUCKET.get()), (blockState)-> true,
                        SoundEvents.BUCKET_FILL_LAVA));
    }

    static void addClayCauldronDefaultClayInteractions(Map<Item, ClayCauldronInteraction> pInteractionsMap){
        pInteractionsMap.put(ModItems.CLAY_WATER_BUCKET.get(), FILL_WATER_CLAY);
        pInteractionsMap.put(ModItems.CLAY_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW_CLAY);
    }
    static void addVanillaCauldronDefaultClayInteractions(Map<Item, CauldronInteraction> pInteractionsMap){
        pInteractionsMap.put(ModItems.CLAY_LAVA_BUCKET.get(), FILL_LAVA);
        pInteractionsMap.put(ModItems.CLAY_WATER_BUCKET.get(), FILL_WATER);
        pInteractionsMap.put(ModItems.CLAY_POWDER_SNOW_BUCKET.get(), FILL_POWDER_SNOW);
    }


    static InteractionResult emptyClayBucket(Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
                                             ItemStack pStack, BlockState pState, SoundEvent pEmptySound) {
        if(!pLevel.isClientSide) {
            Item item = pStack.getItem();
            ItemStack result = pStack == ModItems.CLAY_LAVA_BUCKET.get().getDefaultInstance() ?
                    new ItemStack(ModItems.DAMAGED_CLAY_BUCKET.get()) : new ItemStack(ModItems.CLAY_BUCKET.get());
            pPlayer.setItemInHand(pHand, ItemUtils.createFilledResult(pStack, pPlayer, result));
            pPlayer.awardStat(Stats.FILL_CAULDRON);
            pPlayer.awardStat(Stats.ITEM_USED.get(item));
            pLevel.setBlockAndUpdate(pPos, pState);
            pLevel.playSound(null, pPos, pEmptySound, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent(null, GameEvent.FLUID_PLACE, pPos);
        }

        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }
}
