package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.ModTags;
import dev.tr3ymix.revolution.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public class LevelMixin {

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void destroyBlock(BlockPos pPos, boolean pDropBlock, Entity pEntity, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir) {
        Level level = (Level)(Object)this;
        BlockState state = level.getBlockState(pPos);

        if(state.is(ModTags.MOUNTED_TORCHES)){
            if(!level.isClientSide) {
                Block.popResource(level, pPos, new ItemStack(ModItems.TORCH_BRACKET.get()));
            }
        }
    }
}
