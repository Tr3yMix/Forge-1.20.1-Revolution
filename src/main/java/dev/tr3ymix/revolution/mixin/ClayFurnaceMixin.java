package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.block.entity.ClayFurnaceBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class ClayFurnaceMixin {
    @Inject(
            method = "getTotalCookTime",
            at = @At("RETURN"),
            cancellable = true)
    private static void getTotalCookTime(Level pLevel, AbstractFurnaceBlockEntity pBlockEntity, CallbackInfoReturnable<Integer> cir){
        if(pBlockEntity instanceof ClayFurnaceBlockEntity){
            cir.setReturnValue(cir.getReturnValue() * 4);
        }
    }
}
