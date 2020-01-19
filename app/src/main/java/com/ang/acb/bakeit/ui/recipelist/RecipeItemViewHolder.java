package com.ang.acb.bakeit.ui.recipelist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.databinding.RecipeItemBinding;
import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListFragment.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.RecipeListFragment.EXTRA_RECIPE_NAME;

public class RecipeItemViewHolder extends RecyclerView.ViewHolder {

    private final RecipeItemBinding binding;

    // Required constructor matching super
    public RecipeItemViewHolder(@NonNull RecipeItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static RecipeItemViewHolder create (ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        RecipeItemBinding binding = RecipeItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new RecipeItemViewHolder(binding);
    }

    public void bindTo (Recipe recipe) {
        binding.setRecipe(recipe);
        // Handle recipe item click events.
        binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DetailsActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
            intent.putExtra(EXTRA_RECIPE_NAME, recipe.getName());
            view.getContext().startActivity(intent);
        });

        binding.executePendingBindings();
    }

}
