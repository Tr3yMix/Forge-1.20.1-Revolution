package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.item.ClayBucketItem;
import dev.tr3ymix.revolution.item.FuelItem;
import dev.tr3ymix.revolution.item.KindlingItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, RevolutionMod.MOD_ID);

    public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register("plant_fiber",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> KINDLING = ITEMS.register("kindling",
            () -> new KindlingItem(new Item.Properties()));

    public static final RegistryObject<Item> WOOD = ITEMS.register("wood",
            () -> new FuelItem(new Item.Properties(), 100));

    public static final RegistryObject<Item> DAUB = ITEMS.register("daub",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COW_HIDE = ITEMS.register("cow_hide",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> SHEEP_HIDE = ITEMS.register("sheep_hide",
            () -> new Item(new Item.Properties()));


    public static final RegistryObject<Item> WOODEN_HANDLE = ITEMS.register("wooden_handle",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_PICKAXE_HEAD = ITEMS.register("wooden_pickaxe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_AXE_HEAD = ITEMS.register("wooden_axe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SHOVEL_HEAD = ITEMS.register("wooden_shovel_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_HOE_HEAD = ITEMS.register("wooden_hoe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_SWORD_BLADE = ITEMS.register("wooden_sword_blade",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_PICKAXE_HEAD = ITEMS.register("stone_pickaxe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_AXE_HEAD = ITEMS.register("stone_axe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_SHOVEL_HEAD = ITEMS.register("stone_shovel_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_HOE_HEAD = ITEMS.register("stone_hoe_head",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> STONE_SWORD_BLADE = ITEMS.register("stone_sword_blade",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CRAFTING_HAMMER = ITEMS.register("crafting_hammer",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_CLAY_BUCKET = ITEMS.register("raw_clay_bucket",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CLAY_BUCKET = ITEMS.register("clay_bucket",
            () -> new ClayBucketItem(Fluids.EMPTY, new Item.Properties()));

    public static final RegistryObject<Item> CLAY_WATER_BUCKET = ITEMS.register("clay_water_bucket",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
