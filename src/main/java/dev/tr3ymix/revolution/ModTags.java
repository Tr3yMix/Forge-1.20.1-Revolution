package dev.tr3ymix.revolution;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static dev.tr3ymix.revolution.RevolutionMod.MOD_ID;

public interface ModTags {

    //Biomes
    TagKey<Biome> SPAWNS_BRANCH = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_branch"));
    TagKey<Biome> SPAWNS_ROCK = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_rock"));
    TagKey<Biome> SPAWNS_FLINT_NODULE = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_flint_nodule"));

    //Blocks
    TagKey<Block> BONEMEALABLE_WILD_CROPS = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "bonemealable_wild_crops"));
    TagKey<Block> MOUNTED_TORCHES = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "mounted_torches"));

    TagKey<Item> IGNITER_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "igniter_items"));
}
