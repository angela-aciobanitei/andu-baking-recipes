package com.ang.acb.bakeit.util;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;

import java.util.Arrays;
import java.util.List;

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

    public static List<Recipe> createRecipeList() {
        Recipe carrotCake = TestUtil.createSimpleRecipe(
                5, "Carrot Cake", 4, "");
        carrotCake.setIngredients(Arrays.asList(
                createIngredient(1,"CUP", "foo"),
                createIngredient(5,"TBLSP", "bar")
        ));
        carrotCake.setSteps(Arrays.asList(
                createStep(0, "foo","foo foo", "",""),
                createStep(1, "bar","bar bar", "","")
        ));

        Recipe blueberryPie = TestUtil.createSimpleRecipe(
                6, "Blueberry Pie", 6, "");
        blueberryPie.setIngredients(Arrays.asList(
                createIngredient(2,"CUP", "a"),
                createIngredient(7,"TBLSP", "b"),
                createIngredient(500,"G", "c")
        ));
        blueberryPie.setSteps(Arrays.asList(
                createStep(0, "prepare","foo foo", "",""),
                createStep(1, "bake","bar bar", "",""),
                createStep(2, "serve","bon appetit", "","")
        ));

        return Arrays.asList(carrotCake, blueberryPie);
    }

}
