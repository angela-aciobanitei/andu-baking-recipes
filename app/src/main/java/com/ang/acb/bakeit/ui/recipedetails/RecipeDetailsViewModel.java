package com.ang.acb.bakeit.ui.recipedetails;

import androidx.lifecycle.ViewModel;

import com.ang.acb.bakeit.data.repo.RecipeRepository;

public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository repository;

    public RecipeDetailsViewModel(RecipeRepository repository) {
        this.repository = repository;
    }
}
