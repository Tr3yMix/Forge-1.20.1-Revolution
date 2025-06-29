package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.item.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RevolutionMod.MOD_ID);


    public static final RegistryObject<Item> ROCK_SHARD = ITEMS.register("rock_shard",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KNAPPED_FLINT_NODULE = ITEMS.register("knapped_flint_nodule",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> LONG_HANDLE = ITEMS.register("long_handle",
            () -> new Item(new Item.Properties()));


    //public static final RegistryObject<Item> LAWN_FLAMANGO = ITEMS.register("lawn_flamango",() -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KINDLING = ITEMS.register("kindling",
            () -> new KindlingItem(new Item.Properties()));

    public static final RegistryObject<Item> WOOD = ITEMS.register("wood",
            () -> new FuelItem(new Item.Properties(), 100));

    public static final RegistryObject<Item> THATCH = ITEMS.register("thatch",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DAUB = ITEMS.register("daub",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COW_HIDE = ITEMS.register("cow_hide",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHEEP_HIDE = ITEMS.register("sheep_hide",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_HANDLE = ITEMS.register("wooden_handle",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> WOODEN_SHOVEL_HEAD = ITEMS.register("wooden_shovel_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_HOE_HEAD = ITEMS.register("wooden_hoe_head",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> STONE_SHOVEL_HEAD = ITEMS.register("stone_shovel_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_HOE_HEAD = ITEMS.register("stone_hoe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRAFTING_HAMMER = ITEMS.register("crafting_hammer",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_CLAY_BUCKET = ITEMS.register("raw_clay_bucket",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CLAY_BUCKET = ITEMS.register("clay_bucket",
            () -> new ClayBucketItem(Fluids.EMPTY, new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> DAMAGED_CLAY_BUCKET = ITEMS.register("damaged_clay_bucket",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> PATCHED_CLAY_BUCKET = ITEMS.register("patched_clay_bucket",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CLAY_WATER_BUCKET = ITEMS.register("clay_water_bucket",
            () -> new ClayBucketItem(Fluids.WATER, new Item.Properties().stacksTo(1).craftRemainder(CLAY_BUCKET.get())));

    public static final RegistryObject<Item> CLAY_POWDER_SNOW_BUCKET = ITEMS.register("clay_powder_snow_bucket",
            () -> new SolidClayBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CLAY_LAVA_BUCKET = ITEMS.register("clay_lava_bucket",
            () -> new ClayBucketItem(Fluids.LAVA, new Item.Properties().stacksTo(1).craftRemainder(DAMAGED_CLAY_BUCKET.get()), 20000));

    public static final RegistryObject<Item> CLAY_MILK_BUCKET = ITEMS.register("clay_milk_bucket",
            () -> new ClayMilkBucketItem(new Item.Properties().craftRemainder(CLAY_BUCKET.get()).stacksTo(1)));

    public static final RegistryObject<Item> RAW_CLAY_CRUCIBLE = ITEMS.register("raw_clay_crucible",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CLAY_CRUCIBLE = ITEMS.register("clay_crucible",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CLAY_RAW_IRON_CRUCIBLE = ITEMS.register("clay_raw_iron_crucible",
            () -> new Item(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> CLAY_MOLTEN_IRON_CRUCIBLE = ITEMS.register("clay_molten_iron_crucible",
            () -> new Item(new Item.Properties().stacksTo(16)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
