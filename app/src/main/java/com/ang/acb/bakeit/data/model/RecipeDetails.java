package com.ang.acb.bakeit.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * A simple POJO containing the complete recipe's details, including ingredients and steps.
 *
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
public class RecipeDetails {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Ingredient.class)
    public List<Ingredient> ingredients;

    @Relation(parentColumn = "id", entityColumn = "recipe_id", entity = Step.class)
    public List<Step> steps;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
