package com.ang.acb.bakeit.data.model;

import androidx.core.util.Pair;

import java.util.List;

public class RecipeIngredientsPair {

    private Pair<Recipe, List<Ingredient>> recipeIngredientsPair;

    public RecipeIngredientsPair(Pair<Recipe, List<Ingredient>> recipeIngredientsPair) {
        this.recipeIngredientsPair = recipeIngredientsPair;
    }

    public Pair<Recipe, List<Ingredient>> getRecipeIngredientsPair() {
        return recipeIngredientsPair;
    }

    public void setRecipeIngredientsPair(Pair<Recipe, List<Ingredient>> recipeIngredientsPair) {
        this.recipeIngredientsPair = recipeIngredientsPair;
    }

    public Recipe getRecipe(){
        return recipeIngredientsPair.first;
    }

    public List<Ingredient> getIngredients() {
        return recipeIngredientsPair.second;
    }
}
