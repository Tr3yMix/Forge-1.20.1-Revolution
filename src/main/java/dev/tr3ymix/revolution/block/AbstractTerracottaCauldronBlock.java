package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.core.ClayCauldronInteraction;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class AbstractTerracottaCauldronBlock extends Block {

    private static final VoxelShape INSIDE = box(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
    protected static final VoxelShape SHAPE = Shapes.join(Shapes.block(), Shapes.or(box(0.0D, 0.0D, 4.0D, 16.0D, 3.0D, 12.0D), box(4.0D, 0.0D, 0.0D, 12.0D, 3.0D, 16.0D), box(2.0D, 0.0D, 2.0D, 14.0D, 3.0D, 14.0D), INSIDE), BooleanOp.ONLY_FIRST);
    private final Map<Item, ClayCauldronInteraction> interactions;

    public AbstractTerracottaCauldronBlock(BlockBehaviour.Properties pProperties, Map<Item, ClayCauldronInteraction> pInteractions) {
        super(pProperties);
        this.interactions = pInteractions;
    }

    protected double getContentHeight(BlockState pState) {
        return 0.0D;
    }

    protected boolean isEntityInsideContent(BlockState pState, BlockPos pPos, Entity pEntity) {
        return pEntity.getY() < (double)pPos.getY() + this.getContentHeight(pState) && pEntity.getBoundingBox().maxY > (double)pPos.getY() + 0.25D;
    }

    @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
                                          Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        ClayCauldronInteraction cauldronInteraction = this.interactions.get(itemstack.getItem());
        if(cauldronInteraction == null) {
            return InteractionResult.PASS;
        }
        return cauldronInteraction.interact(pState, pLevel, pPos, pPlayer, pHand, itemstack);
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel,
                                        @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    public @NotNull VoxelShape getInteractionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return INSIDE;
    }

    @SuppressWarnings("deprecation")
    public boolean hasAnalogOutputSignal(@NotNull BlockState pState) {
        return true;
    }

    @SuppressWarnings("deprecation")
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull BlockGetter pLevel,
                                  @NotNull BlockPos pPos, @NotNull PathComputationType pType) {
        return false;
    }

    public abstract boolean isFull(BlockState pState);

    @SuppressWarnings("deprecation")
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos,
                     @NotNull RandomSource pRandom) {
        BlockPos blockpos = PointedDripstoneBlock.findStalactiteTipAboveCauldron(pLevel, pPos);
        if (blockpos != null) {
            Fluid fluid = PointedDripstoneBlock.getCauldronFillFluidType(pLevel, blockpos);
            if (fluid != Fluids.EMPTY && this.canReceiveStalactiteDrip(fluid)) {
                this.receiveStalactiteDrip(pState, pLevel, pPos, fluid);
            }

        }
    }

    protected boolean canReceiveStalactiteDrip(Fluid pFluid) {
        return false;
    }

    protected void receiveStalactiteDrip(BlockState pState, Level pLevel, BlockPos pPos, Fluid pFluid) {
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(ModBlocks.TERRACOTTA_CAULDRON.get());
    }
}
