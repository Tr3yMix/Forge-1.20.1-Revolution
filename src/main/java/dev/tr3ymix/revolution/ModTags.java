package dev.tr3ymix.revolution;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import static dev.tr3ymix.revolution.RevolutionMod.MOD_ID;

public interface ModTags {
    TagKey<Biome> SPAWNS_BRANCH = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_branch"));
    TagKey<Biome> SPAWNS_ROCK = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "spawns_rock"));
}
