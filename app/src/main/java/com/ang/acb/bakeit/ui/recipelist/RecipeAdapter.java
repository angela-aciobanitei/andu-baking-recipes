package com.ang.acb.bakeit.ui.recipelist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeItemViewHolder> {

    private List<Recipe> recipes;

    @NonNull
    @Override
    public RecipeItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecipeItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeItemViewHolder holder, int position) {
        holder.bindTo(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 :  recipes.size();
    }

    public void submitList(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }
}
