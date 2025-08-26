package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.ModTags;
import dev.tr3ymix.revolution.registry.ModItems;
import dev.tr3ymix.revolution.registry.ModSoundOverrides;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "playerDestroy", at = @At("HEAD"), cancellable = true)
    private void onBlockBreak(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (state.is(BlockTags.LOGS)) {
            if (!tool.is(ItemTags.AXES)) {
                ci.cancel();
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
        }

        if(state.is(ModTags.MOUNTED_TORCHES)){
            if(!player.isCreative()) {
                Block.popResource(level, pos, new ItemStack(ModItems.TORCH_BRACKET.get()));
            }
        }


    }

    @Inject(method = "getSoundType", at = @At("HEAD"), cancellable = true)
    private void injectSoundType(BlockState pState, CallbackInfoReturnable<SoundType> cir) {

        /*
        Block self = (Block)(Object)this;

        SoundType override = ModSoundOverrides.getOverrideSound(self);
        if(override != null) {
            cir.setReturnValue(override);
        }

         */

        if(pState.is(ModTags.MOUNTED_TORCHES)){
            cir.setReturnValue(SoundType.LANTERN);
        }

    }




}
