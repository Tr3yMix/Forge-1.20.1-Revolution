package dev.tr3ymix.revolution.core;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.lang.reflect.Field;

public class SoundModifier {


    public static void init() {
        modifySound(Blocks.CLAY, SoundType.MUD);
        modifySound(Blocks.SUGAR_CANE, SoundType.BAMBOO);
        modifySound(Blocks.CAULDRON, SoundType.NETHERITE_BLOCK);
        modifySound(Blocks.LAVA_CAULDRON, SoundType.NETHERITE_BLOCK);
        modifySound(Blocks.WATER_CAULDRON, SoundType.NETHERITE_BLOCK);
        modifySound(Blocks.POWDER_SNOW_CAULDRON, SoundType.NETHERITE_BLOCK);
    }

    private static void modifySound(Block block, SoundType soundType){
        try{
            Field soundTypeField = BlockBehaviour.class.getDeclaredField("soundType");
            soundTypeField.setAccessible(true);

            soundTypeField.set(block, soundType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
