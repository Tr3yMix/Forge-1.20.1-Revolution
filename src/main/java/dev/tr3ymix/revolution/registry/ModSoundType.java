package dev.tr3ymix.revolution.registry;

import dev.imb11.sounds.sound.CustomSounds;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;

@SuppressWarnings("deprecation")
public class ModSoundType {
    public static final SoundType CLAY = new SoundType(1.0F, 1.0F,
            CustomSounds.BLOCK_CLAY_BREAK.get(),
            CustomSounds.BLOCK_CLAY_STEP.get(),
            CustomSounds.BLOCK_CLAY_PLACE.get(),
            CustomSounds.BLOCK_CLAY_HIT.get(),
            CustomSounds.BLOCK_CLAY_STEP.get());

    public static final SoundType MOSSY_BRICK = new SoundType(1.0F, 1.0F,
            CustomSounds.BLOCK_MOSSY_COBBLESTONE_BREAK.get(),
            SoundEvents.DEEPSLATE_BRICKS_STEP,
            SoundEvents.DEEPSLATE_BRICKS_PLACE,
            SoundEvents.DEEPSLATE_BRICKS_HIT,
            SoundEvents.DEEPSLATE_BRICKS_FALL);

    public static final SoundType MOSSY_COBBLESTONE = new SoundType(1.0F, 1.0F,
            CustomSounds.BLOCK_MOSSY_COBBLESTONE_BREAK.get(),
            CustomSounds.BLOCK_MOSSY_COBBLESTONE_STEP.get(),
            CustomSounds.BLOCK_COBBLESTONE_PLACE.get(),
            CustomSounds.BLOCK_MOSSY_COBBLESTONE_HIT.get(),
            CustomSounds.BLOCK_MOSSY_COBBLESTONE_STEP.get());

    public static final SoundType COBBLESTONE = new SoundType(1.0F, 1.0F,
            CustomSounds.BLOCK_COBBLESTONE_BREAK.get(),
            CustomSounds.BLOCK_COBBLESTONE_STEP.get(),
            CustomSounds.BLOCK_COBBLESTONE_PLACE.get(),
            CustomSounds.BLOCK_COBBLESTONE_HIT.get(),
            CustomSounds.BLOCK_COBBLESTONE_STEP.get());

    public static final SoundType ICE = new SoundType(1.0F, 1.0F,
            CustomSounds.BLOCK_ICE_BREAK.get(),
            CustomSounds.BLOCK_ICE_STEP.get(),
            CustomSounds.BLOCK_ICE_PLACE.get(),
            CustomSounds.BLOCK_ICE_HIT.get(),
            CustomSounds.BLOCK_ICE_STEP.get());


}
