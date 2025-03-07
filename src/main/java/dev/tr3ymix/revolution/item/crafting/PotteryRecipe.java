package dev.tr3ymix.revolution.item.crafting;

import com.google.gson.JsonObject;
import dev.tr3ymix.revolution.registry.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PotteryRecipe extends SingleItemRecipe {

    public PotteryRecipe(ResourceLocation pId, String pGroup, Ingredient pIngredient, ItemStack pResult) {
        super(Type.INSTANCE, Serializer.INSTANCE, pId, pGroup, pIngredient, pResult);
    }

    @Override
    public boolean matches(Container container, @NotNull Level level) {
        return this.ingredient.test(container.getItem(0));
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.POTTERY_TABLE.get());
    }


    public static class Type implements RecipeType<PotteryRecipe> {
        private Type(){}
        public static final Type INSTANCE = new Type();
    }

    public static class Serializer implements RecipeSerializer<PotteryRecipe> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull PotteryRecipe fromJson(@NotNull ResourceLocation resourceLocation, @NotNull JsonObject jsonObject) {
            String group = GsonHelper.getAsString(jsonObject, "group", "");
            Ingredient ingredient;
            if(GsonHelper.isArrayNode(jsonObject, "ingredient")){
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(jsonObject, "ingredient"), false);
            }else{
                ingredient = Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"), false);
            }

            String result = GsonHelper.getAsString(jsonObject, "result");
            int count = GsonHelper.getAsInt(jsonObject, "count");
            ItemStack itemStack = new ItemStack(BuiltInRegistries.ITEM.get(new ResourceLocation(result)), count);

            return new PotteryRecipe(resourceLocation, group, ingredient, itemStack);
        }

        @Override
        public @Nullable PotteryRecipe fromNetwork(@NotNull ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            String buf = friendlyByteBuf.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(friendlyByteBuf);
            ItemStack stack = friendlyByteBuf.readItem();
            return new PotteryRecipe(resourceLocation, buf, ingredient, stack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf, PotteryRecipe potteryRecipe) {
            friendlyByteBuf.writeUtf(potteryRecipe.group);
            potteryRecipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(potteryRecipe.result);
        }
    }

}
