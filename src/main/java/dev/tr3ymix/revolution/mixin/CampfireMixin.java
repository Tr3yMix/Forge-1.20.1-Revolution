package dev.tr3ymix.revolution.mixin;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CampfireBlock.class)
public class CampfireMixin {
    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void modifyPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState defaultState = cir.getReturnValue();
        cir.setReturnValue(defaultState.setValue(CampfireBlock.LIT, false));
    }
}
