package dev.tr3ymix.revolution.item;

import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class ClayBucketItem extends BucketItem {

    public ClayBucketItem(Fluid pContent, Properties pProperties) {
        //noinspection deprecation
        super(pContent, pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult hitResult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);

        if(hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            BlockState state = pLevel.getBlockState(pos);

            if(state.is(Blocks.WATER) && state.getFluidState().isSource()) {
                pLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
                pPlayer.setItemInHand(pHand, new ItemStack(ModItems.CLAY_WATER_BUCKET.get()));
                pLevel.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
            }
        }

        return InteractionResultHolder.fail(itemstack);
    }
}
