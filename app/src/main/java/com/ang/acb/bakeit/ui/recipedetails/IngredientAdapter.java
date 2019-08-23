package com.ang.acb.bakeit.ui.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.databinding.IngredientItemBinding;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> ingredientList;

    class IngredientItemViewHolder extends RecyclerView.ViewHolder {

        private IngredientItemBinding binding;

        // Required constructor matching super
        IngredientItemViewHolder(@NonNull IngredientItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Ingredient ingredient) {
            binding.setIngredient(ingredient);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and obtain an instance of the binding class.
        IngredientItemBinding binding = IngredientItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new IngredientItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        ((IngredientItemViewHolder) holder).bindTo(ingredient);
    }

    @Override
    public int getItemCount() {
        return ingredientList == null ? 0 : ingredientList.size();
    }

    public void submitList(List<Ingredient> ingredients) {
        ingredientList = ingredients;
        notifyDataSetChanged();
    }
}
