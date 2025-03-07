package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.ModTags;
import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;


public class ModBiomeModifiers {

    private static final ResourceKey<BiomeModifier> ADD_BRANCH = createKey("add_branch");
    private static final ResourceKey<BiomeModifier> ADD_ROCK = createKey("add_rock");

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        context.register(ADD_BRANCH, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomeTag(context, ModTags.SPAWNS_BRANCH),
                getPlacedFeature(context, ModPlacedFeatures.PATCH_BRANCH), GenerationStep.Decoration.VEGETAL_DECORATION));
        context.register(ADD_ROCK, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomeTag(context, ModTags.SPAWNS_ROCK),
                getPlacedFeature(context, ModPlacedFeatures.PATCH_ROCK), GenerationStep.Decoration.VEGETAL_DECORATION));
    }

    @NotNull
    private static HolderSet.Direct<PlacedFeature> getPlacedFeature(BootstapContext<BiomeModifier> context, ResourceKey<PlacedFeature> placedFeature) {
        return HolderSet.direct(context.lookup(Registries.PLACED_FEATURE).getOrThrow(placedFeature));
    }

    @NotNull
    private static HolderSet.Named<Biome> biomeTag(BootstapContext<BiomeModifier> context, TagKey<Biome> tag) {
        return context.lookup(Registries.BIOME).getOrThrow(tag);
    }

    public static ResourceKey<BiomeModifier> createKey(String string) {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(RevolutionMod.MOD_ID, string));
    }
}
