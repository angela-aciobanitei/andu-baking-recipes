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
 * Immutable model class for a Step.
 *
 * See: https://developer.android.com/training/data-storage/room/relationships#one-to-many
 * See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
@Entity(tableName = "step",
        foreignKeys = @ForeignKey(
                entity = Recipe.class,
                parentColumns = "id",
                childColumns = "recipe_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        ),
        indices = { @Index(value = {"recipe_id"})}
)
public class Step {

    @NonNull
    @PrimaryKey
    @SerializedName("id")
    private Integer id;

    @NonNull
    @ColumnInfo(name = "recipe_id")
    private Integer recipeId;

    @SerializedName("shortDescription")
    private String shortDescription;

    @SerializedName("description")
    private String description;

    @SerializedName("videoURL")
    private String videoURL;

    @SerializedName("thumbnailURL")
    private String thumbnailURL;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    @NotNull
    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NotNull Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
}
