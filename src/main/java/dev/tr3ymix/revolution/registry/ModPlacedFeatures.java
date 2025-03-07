package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> PATCH_BRANCH = createKey("patch_branch");
    public static final ResourceKey<PlacedFeature> PATCH_ROCK = createKey("patch_rock");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {

        register(context, PATCH_BRANCH, ModConfiguredFeatures.PATCH_BRANCH, VegetationPlacements.worldSurfaceSquaredWithCount(2));
        register(context, PATCH_ROCK, ModConfiguredFeatures.PATCH_ROCK, VegetationPlacements.worldSurfaceSquaredWithCount(2));
    }

    public static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> resourceKey, ResourceKey<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> placementModifiers) {
        context.register(resourceKey, new PlacedFeature(context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(configuredFeature), List.copyOf(placementModifiers)));
    }

    public static ResourceKey<PlacedFeature> createKey(String string) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(RevolutionMod.MOD_ID, string));
    }
}
