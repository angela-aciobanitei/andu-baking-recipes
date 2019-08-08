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
public class RecipeDetails extends Recipe {

    @Embedded(prefix = "recipe_")
    public Recipe recipe = null;

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Ingredient> ingredients = new ArrayList<>();

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Step> steps = new ArrayList<>();
}
