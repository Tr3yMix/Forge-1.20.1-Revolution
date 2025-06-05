package dev.tr3ymix.revolution.mixin;

import dev.tr3ymix.revolution.registry.ModSoundOverrides;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class SoundModifierMixin {

    @Inject(method = "getSoundType", at = @At("HEAD"), cancellable = true)
    private void injectSoundType(CallbackInfoReturnable<SoundType> cir) {
        Block self = (Block)(Object)this;

        SoundType override = ModSoundOverrides.getOverrideSound(self);
        if(override != null) {
            cir.setReturnValue(override);
        }

    }
}
