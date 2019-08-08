package com.ang.acb.bakeit.data.remote;

import androidx.lifecycle.LiveData;

import com.ang.acb.bakeit.data.model.Recipe;

import java.util.List;

import retrofit2.http.GET;

/**
 * Defines the REST API access points for Retrofit.
 */
public interface ApiService {

    @GET("baking.json")
    LiveData<ApiResponse<List<Recipe>>> getAllRecipes();
}
