package dev.tr3ymix.revolution.item;

import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

import static net.minecraft.world.level.block.LiquidBlock.LEVEL;

public class ClayBucketItem extends Item implements DispensibleContainerItem {

    private final int burnTime;

    private final Fluid content;
    private final Supplier<? extends Fluid> fluidSupplier;

    public ClayBucketItem(Fluid pContent, Properties pProperties) {
        //noinspection deprecation
        super(pProperties);
        this.content = pContent;
        this.fluidSupplier = ForgeRegistries.FLUIDS.getDelegateOrThrow(pContent);
        this.burnTime = -1;
    }

    public ClayBucketItem(Fluid pContent, Properties pProperties, int burnTime) {
        //noinspection deprecation
        super(pProperties);
        this.content = pContent;
        this.fluidSupplier = ForgeRegistries.FLUIDS.getDelegateOrThrow(pContent);
        this.burnTime = burnTime;
    }

    public ClayBucketItem(Supplier<? extends Fluid> pSupplier, Properties pProperties) {
        super(pProperties);
        this.content = null;
        this.fluidSupplier = pSupplier;
        this.burnTime = -1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack stack = pPlayer.getItemInHand(pHand);
        BlockHitResult hitResult = getPlayerPOVHitResult(pLevel, pPlayer, this.content == Fluids.EMPTY ?
                ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);

        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onBucketUse(pPlayer, pLevel, stack, hitResult);



        if(ret != null) return ret;

        if(hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        } else if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }
        else{
            BlockPos pos = hitResult.getBlockPos();
            Direction direction = hitResult.getDirection();
            BlockPos relativePos = pos.relative(direction);
            if(pLevel.mayInteract(pPlayer, pos) && pPlayer.mayUseItemAt(relativePos, direction, stack)) {
                if(this.content == Fluids.EMPTY) {
                    BlockState state = pLevel.getBlockState(pos);
                    if(state.getBlock() instanceof BucketPickup bucketPickup){
                        ItemStack pickupStack = pickupBlock(pLevel, pos, state, bucketPickup);

                        if(!pickupStack.isEmpty()) {
                            pPlayer.awardStat(Stats.ITEM_USED.get(this));
                            bucketPickup.getPickupSound(state).ifPresent((soundEvent ->
                                    pPlayer.playSound(soundEvent, 1.0F, 1.0F)));

                            pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, pos);
                            ItemStack resultStack = ItemUtils.createFilledResult(stack, pPlayer, pickupStack);
                            if(!pLevel.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer, pickupStack);
                            }

                            return InteractionResultHolder.sidedSuccess(resultStack, pLevel.isClientSide());
                        }
                    }
                    return InteractionResultHolder.fail(stack);
                } else {
                    BlockState state = pLevel.getBlockState(pos);
                    BlockPos correctedPos = canBlockContainFluid(pLevel, pos, state) ? pos : relativePos;
                    if(this.emptyContents(pPlayer, pLevel, correctedPos, hitResult, stack)){
                        this.checkExtraContent(pPlayer, pLevel, stack, correctedPos);
                        if(pPlayer instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) pPlayer, correctedPos, stack);
                        }

                        pPlayer.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, pPlayer), pLevel.isClientSide());
                    } else {
                        return InteractionResultHolder.fail(stack);
                    }
                }
            }else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }

    private ItemStack pickupBlock(Level pLevel, BlockPos pPos, BlockState pState, BucketPickup bucketPickup) {
        if (bucketPickup == Blocks.WATER) {
            if (pState.getValue(LEVEL) == 0) {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
                return new ItemStack(ModItems.CLAY_WATER_BUCKET.get());
            } else {
                return ItemStack.EMPTY;
            }
        }
        else if (bucketPickup == Blocks.LAVA) {
            if (pState.getValue(LEVEL) == 0) {
                pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
                return new ItemStack(ModItems.CLAY_LAVA_BUCKET.get());
            } else {
                return ItemStack.EMPTY;
            }
        }
        else if (bucketPickup == Blocks.POWDER_SNOW) {
            pLevel.setBlock(pPos, Blocks.AIR.defaultBlockState(), 11);
            if (!pLevel.isClientSide()) {
                pLevel.levelEvent(2001, pPos, Block.getId(pState));
            }

            return new ItemStack(ModItems.CLAY_POWDER_SNOW_BUCKET.get());
        } else {
            return ItemStack.EMPTY;
        }

    }

    public static ItemStack getEmptySuccessItem(ItemStack pBucketStack, Player pPlayer) {
        ItemStack result = pBucketStack == ModItems.CLAY_LAVA_BUCKET.get().getDefaultInstance() ?
                new ItemStack(ModItems.CLAY_BUCKET.get()) : new ItemStack(ModItems.DAMAGED_CLAY_BUCKET.get());
        return !pPlayer.getAbilities().instabuild ? result : pBucketStack;
    }

    @Override
    public void checkExtraContent(@Nullable Player pPlayer, @NotNull Level pLevel, @NotNull ItemStack pContainerStack, @NotNull BlockPos pPos) {
    }

    @Override
    public boolean emptyContents(@Nullable Player pPlayer, @NotNull Level pLevel, @NotNull BlockPos pPos, @Nullable BlockHitResult pResult) {
        return this.emptyContents(pPlayer, pLevel, pPos, pResult, null);
    }

    @Override
    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hitResult, @Nullable ItemStack container) {
        if(!(this.content instanceof FlowingFluid)){
            return false;
        }else{
            BlockState state = level.getBlockState(pos);
            Block block = state.getBlock();
            boolean canBePlaced = state.canBeReplaced(this.content);
            boolean flag = state.isAir() || canBePlaced || block instanceof LiquidBlockContainer &&
                    ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, state, this.content);

            Optional<FluidStack> containedFluidStack = Optional.ofNullable(container).flatMap(FluidUtil::getFluidContained);
            if(!flag){
                return hitResult != null && this.emptyContents(player, level, hitResult.getBlockPos().relative(hitResult.getDirection()), null, container);
            } else if(containedFluidStack.isPresent() && this.content.getFluidType().isVaporizedOnPlacement(level, pos, containedFluidStack.get())){
                this.content.getFluidType().onVaporize(player, level, pos, containedFluidStack.get());
                return true;
            } else //noinspection deprecation
                if (level.dimensionType().ultraWarm() && this.content.is(FluidTags.WATER)) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS,
                        0.5F, 2.5F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);
                for (int i = 0; i < 8; ++i){
                    level.addParticle(ParticleTypes.LARGE_SMOKE,
                            (double) x + Math.random(),
                            (double) y + Math.random(),
                            (double) z + Math.random(),
                            0.0D, 0.0D, 0.0D);
                }
                return true;
            } else if (block instanceof LiquidBlockContainer && ((LiquidBlockContainer) block).canPlaceLiquid(level, pos, state, this.content)) {
                ((LiquidBlockContainer) block).placeLiquid(level, pos, state, ((FlowingFluid)this.content).getSource(false));
                this.playEmptySound(player, level, pos);
                return true;
            } else {
                    //noinspection deprecation
                    if (!level.isClientSide && canBePlaced && !state.liquid()) {
                        level.destroyBlock(pos, true);
                    }
                    if(!level.setBlock(pos, this.content.defaultFluidState().createLegacyBlock(), 11) && !state.getFluidState().isSource()){
                        return false;
                    } else {
                        this.playEmptySound(player, level, pos);
                        return true;
                    }
                }
        }
    }

    protected void playEmptySound(@Nullable Player pPlayer, Level pLevel, BlockPos pPos) {
        SoundEvent soundEvent = this.content.getFluidType().getSound(pPlayer, pLevel, pPos, SoundActions.BUCKET_EMPTY);
        if(soundEvent == null) //noinspection deprecation
            soundEvent = this.content.is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        pLevel.playSound(pPlayer, pPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
    }

    protected boolean canBlockContainFluid(Level pLevel, BlockPos pPos, BlockState pState) {
        return pState.getBlock() instanceof LiquidBlockContainer && ((LiquidBlockContainer) pState.getBlock()).canPlaceLiquid(pLevel, pPos, pState, this.content);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return burnTime;
    }
}
