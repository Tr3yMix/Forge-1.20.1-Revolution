package dev.tr3ymix.revolution.registry;

import dev.tr3ymix.revolution.RevolutionMod;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, RevolutionMod.MOD_ID);
    }

    @Override
    protected void start() {
    }
}
