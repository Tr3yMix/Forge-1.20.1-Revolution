package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraftforge.fml.common.Mod;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BRANCH = createKey("patch_branch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_ROCK = createKey("patch_rock");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_REEDS = createKey("patch_reeds");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        registerConfiguredFeature(context, PATCH_BRANCH, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(3, PlacementUtils.onlyWhenEmpty(
                ModFeatures.LAYER_BLOCK.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.BRANCH.get()))
        )));
        registerConfiguredFeature(context, PATCH_ROCK, Feature.RANDOM_PATCH, FeatureUtils.simpleRandomPatchConfiguration(2, PlacementUtils.onlyWhenEmpty(
                ModFeatures.LAYER_BLOCK.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ROCK.get()))
        )));

        registerConfiguredFeature(context, PATCH_REEDS, Feature.RANDOM_PATCH, new RandomPatchConfiguration(150, 10, 2, PlacementUtils.onlyWhenEmpty(
                ModFeatures.LAYER_BLOCK.get(), new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.REEDS.get()))
        )));
    }


    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerConfiguredFeature
            (BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> resourceKey, F feature, FC featureConfiguration) {
        context.register(resourceKey, new ConfiguredFeature<>(feature, featureConfiguration));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> createKey(String string) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(RevolutionMod.MOD_ID, string));
    }
}
