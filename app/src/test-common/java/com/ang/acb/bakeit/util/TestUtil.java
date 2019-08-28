package com.ang.acb.bakeit.util;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;

import java.util.List;

public class TestUtil {

    public static Recipe createSimpleRecipe(String name, Integer servings) {
        return new Recipe(Recipe.UNKNOWN_ID, name, servings, "");
    }

    public static Recipe createSimpleRecipe(Integer recipeId, String name, Integer servings) {
        return new Recipe(recipeId, name, servings, "");
    }

    public static Ingredient createIngredient(Integer ingredientId, Integer recipeId, double quantity,
                                              String measure, String ingredient) {
        return new Ingredient(ingredientId, recipeId, quantity, measure, ingredient);
    }

    public static Step createStep(Integer stepId, Integer recipeId, String shortDescription,
                                  String description, String videoURL, String thumbnailURL) {
        return new Step(stepId, recipeId, shortDescription, description,
                videoURL, thumbnailURL);
    }

    public static WholeRecipe createWholeRecipe (String name, Integer servings, String image,
                                                 List<Ingredient> ingredients, List<Step> steps) {
        WholeRecipe wholeRecipe = new WholeRecipe();
        wholeRecipe.setRecipe(new Recipe(Recipe.UNKNOWN_ID, name, servings, null));
        wholeRecipe.setIngredients(ingredients);
        wholeRecipe.setSteps(steps);

        return wholeRecipe;
    }
}
