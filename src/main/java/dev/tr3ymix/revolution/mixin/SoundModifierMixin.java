package dev.tr3ymix.revolution.mixin;

import com.ninni.twigs.registry.TwigsBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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

        if(self == Blocks.CLAY){
            cir.setReturnValue(SoundType.MUD);
        }else if(self == Blocks.SUGAR_CANE){
            cir.setReturnValue(SoundType.BAMBOO);
        }else if(self == Blocks.CAULDRON || self == Blocks.LAVA_CAULDRON ||
                self == Blocks.WATER_CAULDRON || self == Blocks.POWDER_SNOW){
            cir.setReturnValue(SoundType.NETHERITE_BLOCK);
        }else if(self == TwigsBlocks.TWIG.get()){
            cir.setReturnValue(SoundType.MANGROVE_ROOTS);
        } else if (self == TwigsBlocks.PEBBLE.get()) {
            cir.setReturnValue(SoundType.BASALT);
        }
    }
}
