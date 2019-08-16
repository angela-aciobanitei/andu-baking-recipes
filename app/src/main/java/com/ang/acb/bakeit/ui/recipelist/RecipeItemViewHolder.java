package com.ang.acb.bakeit.ui.recipelist;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.databinding.RecipeItemBinding;
import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.ARG_RECIPE_ID;

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
            intent.putExtra(ARG_RECIPE_ID, recipe.getId());
            view.getContext().startActivity(intent);
        });

        // Note: when a variable or observable object changes, the binding is scheduled
        // to change before the next frame. There are times, however, when binding must
        // be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }

}
