package com.ang.acb.bakeit.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Immutable model class for a Step.
 */
@Entity(tableName = "steps")
public class Step {

    public static final int UNKNOWN_ID = -3;

    @PrimaryKey(autoGenerate = true)
    private Integer roomId;

    @SerializedName("id")
    @Expose
    private Integer id;

    @ColumnInfo(name = "recipe_id")
    @Expose
    private Integer recipeId;

    @ColumnInfo(name = "short_description")
    @SerializedName("shortDescription")
    @Expose
    private String shortDescription;

    @SerializedName("description")
    @Expose
    private String description;

    @ColumnInfo(name = "video_url")
    @SerializedName("videoURL")
    @Expose
    private String videoURL;

    @ColumnInfo(name = "thumbnail_url")
    @SerializedName("thumbnailURL")
    @Expose
    private String thumbnailURL;

    @Ignore
    public Step(Integer id, Integer recipeId, String shortDescription,
                String description, String videoURL, String thumbnailURL) {
        this.id = id;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    @Ignore
    public Step(Integer id, String shortDescription, String description,
                String videoURL, String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

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
