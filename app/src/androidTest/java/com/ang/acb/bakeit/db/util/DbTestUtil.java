package com.ang.acb.bakeit.db.util;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;

import java.util.ArrayList;
import java.util.List;

public class DbTestUtil {

    public static Recipe createSimpleRecipe(Integer id, String name, Integer servings, String image,
                                            List<Ingredient> ingredients, List<Step> steps) {
        return new Recipe(Recipe.UNKNOWN_ID, name, servings, null, null, null);
    }

    public static Ingredient createIngredient(Integer id, Integer recipeId, double quantity,
                                              String measure, String ingredient) {
        return new Ingredient(Ingredient.UNKNOWN_ID, recipeId, quantity, measure, ingredient);
    }

    public static Step createStep(Integer id, Integer recipeId, String shortDescription,
                                  String description, String videoURL, String thumbnailURL) {
        return new Step(Step.UNKNOWN_ID, recipeId, shortDescription, description,
                videoURL, thumbnailURL);
    }

    public static WholeRecipe createWholeRecipe (Integer id, String name, Integer servings, String image,
                                                 List<Ingredient> ingredients, List<Step> steps) {
        WholeRecipe wholeRecipe = new WholeRecipe();
        wholeRecipe.setRecipe(new Recipe(Recipe.UNKNOWN_ID, name, servings, null, null, null));
        wholeRecipe.setIngredients(ingredients);
        wholeRecipe.setSteps(steps);

        return wholeRecipe;
    }
}
