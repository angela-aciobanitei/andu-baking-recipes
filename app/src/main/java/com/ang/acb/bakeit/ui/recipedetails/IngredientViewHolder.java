package com.ang.acb.bakeit.ui.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.databinding.IngredientItemBinding;

public class IngredientViewHolder extends RecyclerView.ViewHolder {

    private IngredientItemBinding binding;

    // Required constructor matching super
    public IngredientViewHolder(@NonNull IngredientItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static IngredientViewHolder create (ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        IngredientItemBinding binding = IngredientItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new IngredientViewHolder(binding);
    }

    public void bindTo (Ingredient ingredient) {
        binding.ingredientItemQuantity.setText(String.valueOf(ingredient.getQuantity()));
        binding.ingredientItemMeasure.setText(ingredient.getMeasure());
        binding.ingredientItemName.setText(ingredient.getIngredient());

        // Binding must be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }

}
