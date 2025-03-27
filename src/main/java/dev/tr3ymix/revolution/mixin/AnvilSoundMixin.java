package dev.tr3ymix.revolution.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundType.class)
public class AnvilSoundMixin {
    @SuppressWarnings("deprecation")
    @Shadow @Mutable
    public static final SoundType ANVIL = new SoundType(
            0.3F,
            1.0F,
            SoundEvents.NETHERITE_BLOCK_BREAK,
            SoundEvents.NETHERITE_BLOCK_STEP,
            SoundEvents.ANVIL_PLACE,
            SoundEvents.NETHERITE_BLOCK_HIT,
            SoundEvents.NETHERITE_BLOCK_FALL
    );
}
