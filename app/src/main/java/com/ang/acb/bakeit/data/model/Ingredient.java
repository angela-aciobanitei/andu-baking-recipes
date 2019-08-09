package com.ang.acb.bakeit.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Immutable model class for an Ingredient.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#one-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "ingredient",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        indices = {@Index(value = {"recipe_id"})}
)
public class Ingredient {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private Long id;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private Long recipeId;

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("measure")
    private String measure;

    @SerializedName("ingredient")
    private String ingredient;

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    @NotNull
    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NotNull Long recipeId) {
        this.recipeId = recipeId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }
}
