package com.ang.acb.bakeit.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * A simple POJO containing the complete recipe's details, including ingredients and steps.
 *
 * See: https://developer.android.com/reference/android/arch/persistence/room/Relation
 * See: https://developer.android.com/reference/android/arch/persistence/room/Embedded
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
public class RecipeDetails {

    // The @Embedded annotation can be used on a field of an Entity or POJO to
    // signal that nested fields (i.e. fields of the annotated field's class)
    // can be referenced directly in the SQL queries. If the container is an
    // Entity, these sub fields will be columns in the Entity's database table.
    @Embedded
    public Recipe recipe;

    // The @Relation annotation can be used in a Pojo to automatically fetch
    // relation entities. When the Pojo is returned from a query, all of its
    // relations are also fetched by Room. An @Relation annotated field cannot
    // be a constructor parameter, it must be public or have a public setter.
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
