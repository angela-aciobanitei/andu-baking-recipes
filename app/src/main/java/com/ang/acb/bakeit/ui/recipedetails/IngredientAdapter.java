package com.ang.acb.bakeit.ui.recipedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Ingredient> ingredientList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return IngredientViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        ((IngredientViewHolder) holder).bindTo(ingredient);
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
