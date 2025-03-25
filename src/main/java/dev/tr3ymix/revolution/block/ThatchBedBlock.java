package dev.tr3ymix.revolution.block;

import dev.tr3ymix.revolution.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ThatchBedBlock extends BedBlock {


    public ThatchBedBlock(Properties pProperties) {
        super(DyeColor.GREEN, pProperties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer,
                                          @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if(pLevel.isClientSide()) return InteractionResult.SUCCESS;

        if(checkForCampfires(pLevel, pPos)) {
            return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        }
        else{
            if(pPlayer instanceof ServerPlayer player){
                player.displayClientMessage(Component.literal("You cannot sleep unless a lit campfire is nearby!"), true);
            }
            return InteractionResult.PASS;
        }
    }

    public boolean checkForCampfires(Level pLevel, BlockPos pPos) {
        int radius = Config.primitive_bed_campfire_radius;

        for(BlockPos pos : BlockPos.betweenClosed(pPos.offset(-radius, -1, -radius), pPos.offset(radius, 1, radius))) {
            BlockState state = pLevel.getBlockState(pos);
            if(state.getBlock() instanceof CampfireBlock && state.getValue(CampfireBlock.LIT)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Block.box(0, 0, 0, 16, 2, 16);
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }
}
