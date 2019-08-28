package com.ang.acb.bakeit.util;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;

public class TestUtil {

    public static Recipe createSimpleRecipe(Integer recipeId, String name, Integer servings, String image) {
       Recipe recipe =  new Recipe();
       recipe.setId(recipeId);
       recipe.setName(name);
       recipe.setServings(servings);
       recipe.setImage(image);
       return recipe;
    }

    public static Ingredient createIngredient(double quantity, String measure, String ingredient) {
        return new Ingredient(quantity, measure, ingredient);
    }

    public static Step createStep(Integer index, String shortDescription, String description, String videoURL, String thumbnailURL) {
        return new Step(index, shortDescription, description,videoURL, thumbnailURL);
    }

}
