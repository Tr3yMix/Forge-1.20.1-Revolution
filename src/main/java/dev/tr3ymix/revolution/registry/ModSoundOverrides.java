package dev.tr3ymix.revolution.registry;

import com.ninni.twigs.registry.TwigsBlocks;
import com.ninni.twigs.registry.TwigsSoundEvents;
import com.ordana.immersive_weathering.ImmersiveWeathering;
import com.ordana.immersive_weathering.reg.ModBlocks;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.*;

public class ModSoundOverrides {
    private static final Map<SoundType, Set<Block>> SOUND_OVERRIDES = new HashMap<>();

    public static void assignSoundOverride(SoundType soundType, Block... blocks) {
        SOUND_OVERRIDES.computeIfAbsent(soundType, k -> new HashSet<>()).addAll(Arrays.asList(blocks));
    }

    public static SoundType getOverrideSound(Block block) {
        for(Map.Entry<SoundType, Set<Block>> entry : SOUND_OVERRIDES.entrySet()) {
            if(entry.getValue().contains(block)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static void registerOverrides() {
        assignSoundOverride(SoundType.BAMBOO, Blocks.SUGAR_CANE);
        assignSoundOverride(SoundType.SAND, Blocks.REDSTONE_WIRE);
        assignSoundOverride(SoundType.NETHERITE_BLOCK,
                Blocks.CAULDRON,
                Blocks.LAVA_CAULDRON,
                Blocks.WATER_CAULDRON,
                Blocks.POWDER_SNOW_CAULDRON);
        assignSoundOverride(SoundType.NETHER_BRICKS,
                TwigsBlocks.MIXED_BRICKS.get(),
                TwigsBlocks.CHISELED_BRICKS.get(),
                TwigsBlocks.CRACKED_BRICKS.get(),
                TwigsBlocks.COBBLESTONE_BRICKS.get(),
                TwigsBlocks.COBBLESTONE_BRICK_STAIRS.get(),
                TwigsBlocks.COBBLESTONE_BRICK_SLAB.get(),
                TwigsBlocks.COBBLESTONE_BRICK_WALL.get(),
                TwigsBlocks.CRACKED_COBBLESTONE_BRICKS.get(),
                TwigsBlocks.SILT_BRICKS.get(),
                TwigsBlocks.SILT_BRICK_STAIRS.get(),
                TwigsBlocks.SILT_BRICK_SLAB.get(),
                TwigsBlocks.SILT_BRICK_WALL.get(),
                TwigsBlocks.MIXED_SILT_BRICKS.get(),
                TwigsBlocks.CRACKED_SILT_BRICKS.get(),
                TwigsBlocks.CHISELED_SILT_BRICKS.get()
                );
        assignSoundOverride(ModSoundType.MOSSY_BRICK,
                TwigsBlocks.MOSSY_BRICKS.get(),
                TwigsBlocks.MOSSY_BRICK_SLAB.get(),
                TwigsBlocks.MOSSY_BRICK_STAIRS.get(),
                TwigsBlocks.MOSSY_BRICK_WALL.get(),
                TwigsBlocks.MOSSY_COBBLESTONE_BRICKS.get(),
                TwigsBlocks.MOSSY_COBBLESTONE_BRICK_STAIRS.get(),
                TwigsBlocks.MOSSY_COBBLESTONE_BRICK_SLAB.get(),
                TwigsBlocks.MOSSY_COBBLESTONE_BRICK_WALL.get()
        );
        assignSoundOverride(SoundType.MANGROVE_ROOTS, TwigsBlocks.TWIG.get());
        assignSoundOverride(SoundType.BASALT, TwigsBlocks.PEBBLE.get());
        assignSoundOverride(ModSoundType.ICE, ModBlocks.ICICLE.get(), ModBlocks.THIN_ICE.get());
        assignSoundOverride(TwigsSoundEvents.SILT,
                ModBlocks.SILT.get(),
                ModBlocks.GRASSY_SILT.get(),
                ModBlocks.SILTY_FARMLAND.get());
        assignSoundOverride(SoundType.DECORATED_POT, Blocks.FLOWER_POT);
        
    }
}
