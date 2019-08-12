package com.ang.acb.bakeit.data.model;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

/**
 * A simple POJO containing the complete recipe's details, including ingredients and steps.
 *
 * See: https://medium.com/@magdamiu/android-room-persistence-library-relations-75bbe02e8522
 */
public class RecipeDetails  {

    @Embedded(prefix = "recipe_")
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Step> steps;

    public RecipeDetails(){}

    public RecipeDetails(Recipe recipe, List<Ingredient> ingredients, List<Step> steps) {
        this.recipe = recipe;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
