package dev.tr3ymix.revolution.registry;

import com.ninni.twigs.registry.TwigsItems;
import com.ordana.immersive_weathering.blocks.cracked.Crackable;
import com.ordana.immersive_weathering.blocks.cracked.CrackableBlock;
import com.ordana.immersive_weathering.blocks.cracked.CrackedBlock;
import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.block.*;
import dev.tr3ymix.revolution.core.ClayCauldronInteraction;
import dev.tr3ymix.revolution.item.FuelBlockItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, RevolutionMod.MOD_ID);


    public static final RegistryObject<Block> BRANCH = registerFuelBlock("branch",
            () -> new FloorItemBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instabreak().noOcclusion().sound(SoundType.MANGROVE_ROOTS).noCollission()), 100);

    public static final RegistryObject<Block> ROCK = registerBlock("rock",
            () -> new FloorItemBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instabreak().noOcclusion().sound(ModSoundType.COBBLESTONE).noCollission()));

    public static final RegistryObject<Block> WILD_FLAX = registerBlock("wild_flax",
            ()-> new DoublePlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)));

    public static final RegistryObject<Block> REED_SPROUT = registerBlock("reed_sprout",
            () -> new ReedSproutBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ).ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> REEDS = registerBlock("reeds",
            () -> new DoublePlantBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable()
                    .noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ)
                    .ignitedByLava().pushReaction(PushReaction.DESTROY)));

    public static final RegistryObject<Block> THATCH_BLOCK = registerBlock("thatch_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.2F).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> THATCH_SLAB = registerBlock("thatch_slab_block",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(THATCH_BLOCK.get()).noOcclusion()));

    public static final RegistryObject<Block> THATCH_BED = registerBlock("thatch_bed",
            () -> new ThatchBedBlock(BlockBehaviour.Properties.copy(Blocks.GRAY_BED).sound(SoundType.MANGROVE_ROOTS).noOcclusion()));

    public static final RegistryObject<Block> PLANT_FIBER_BLOCK = registerBlock("plant_fiber_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).sound(SoundType.GRASS)));

    public static final RegistryObject<Block> PLANT_FIBER_CUSHION = registerBlock("plant_fiber_cushion",
            () -> new PlantFiberCushionBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).noOcclusion()));

    public static final RegistryObject<Block> POTTERY_TABLE = registerBlock("pottery_table",
            () -> new PotteryTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE).sound(SoundType.WOOD)));


    public static final RegistryObject<Block> CLAY_FURNACE = registerBlock("clay_furnace",
            () -> new ClayFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.CLAY).sound(ModSoundType.CLAY).noOcclusion()));

    public static final RegistryObject<Block> TERRACOTTA_FURNACE = registerBlock("terracotta_furnace",
            () -> new TerracottaFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).sound(SoundType.DECORATED_POT).noOcclusion().lightLevel(litBlockEmission(13))));

    public static final RegistryObject<Block> CLAY_CAULDRON = registerBlock("clay_cauldron",
            () -> new ClayCauldronBlock(BlockBehaviour.Properties.copy(Blocks.CLAY).sound(ModSoundType.CLAY).noOcclusion()));

    public static final RegistryObject<Block> TERRACOTTA_CAULDRON = registerBlock("terracotta_cauldron",
            () -> new TerracottaCauldronBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).sound(SoundType.DECORATED_POT).noOcclusion()));

    public static final RegistryObject<Block> WATER_TERRACOTTA_CAULDRON = registerBlock("water_terracotta_cauldron",
            () -> new TerracottaLayeredCauldronBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).sound(SoundType.DECORATED_POT).noOcclusion(),
                    TerracottaLayeredCauldronBlock.RAIN, ClayCauldronInteraction.WATER));

    public static final RegistryObject<Block> POWDER_SNOW_TERRACOTTA_CAULDRON = registerBlock("powder_snow_terracotta_cauldron",
            () -> new TerracottaLayeredCauldronBlock(BlockBehaviour.Properties.copy(Blocks.FLOWER_POT).sound(SoundType.DECORATED_POT).noOcclusion(),
                    TerracottaLayeredCauldronBlock.SNOW, ClayCauldronInteraction.POWDER_SNOW));

    public static final RegistryObject<Block> WATTLE_AND_DAUB = registerBlock("wattle_and_daub",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.MUD).sound(SoundType.MUDDY_MANGROVE_ROOTS)));

    public static final RegistryObject<Block> WATTLE_AND_DAUB_DRY = registerBlock("wattle_and_daub_dry",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.PACKED_MUD).sound(SoundType.PACKED_MUD)));


    @SuppressWarnings("SameParameterValue")
    private static ToIntFunction<BlockState> litBlockEmission(int pLightLevel){
        return (state) -> (boolean) state.getValue(BlockStateProperties.LIT) ? pLightLevel : 0;
    }


    private static <T extends Block>RegistryObject<T> registerFuelBlock(String name, Supplier<T> block, int burnTime) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerFuelBlockItem(name, toReturn, burnTime);
        return toReturn;
    }

    private static <T extends Block> void registerFuelBlockItem(String name, RegistryObject<T> block, int burnTime) {
        ModItems.ITEMS.register(name, () -> new FuelBlockItem(block.get(), new Item.Properties(), burnTime));
    }


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
