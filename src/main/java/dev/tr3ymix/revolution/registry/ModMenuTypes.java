package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import dev.tr3ymix.revolution.inventory.PotteryMenu;
import dev.tr3ymix.revolution.inventory.ClayFurnaceMenu;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, RevolutionMod.MOD_ID);

    public static final RegistryObject<MenuType<ClayFurnaceMenu>> CLAY_FURNACE_MENU =
            registerMenuType(ClayFurnaceMenu::new, "clay_furnace_menu");



    public static final RegistryObject<MenuType<PotteryMenu>> POTTERY_MENU =
            registerMenuType(PotteryMenu::new, "pottery_menu");


    @SuppressWarnings("SameParameterValue")
    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(IContainerFactory<T> factory, String name) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }


}
