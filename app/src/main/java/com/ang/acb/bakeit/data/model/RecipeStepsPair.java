package com.ang.acb.bakeit.data.model;

import androidx.core.util.Pair;

import java.util.List;

public class RecipeStepsPair {

    private Pair<Recipe, List<Step>> recipeStepsPair;

    public RecipeStepsPair(Pair<Recipe, List<Step>> recipeStepsPair) {
        this.recipeStepsPair = recipeStepsPair;
    }

    public Pair<Recipe, List<Step>> getRecipeStepsPair() {
        return recipeStepsPair;
    }

    public void setRecipeStepsPair(Pair<Recipe, List<Step>> recipeStepsPair) {
        this.recipeStepsPair = recipeStepsPair;
    }

    public Recipe getRecipe() {
        return recipeStepsPair.first;
    }

    public List<Step> getSteps() {
        return recipeStepsPair.second;
    }
}
