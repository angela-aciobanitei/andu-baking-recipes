package com.ang.acb.bakeit.ui.recipelist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Recipe> recipeList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecipeItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        ((RecipeItemViewHolder) holder).bindTo(recipe);
    }

    @Override
    public int getItemCount() {
        return recipeList == null ? 0 :  recipeList.size();
    }

    public void setRecipeList(List<Recipe> list) {
        this.recipeList = list;
        notifyDataSetChanged();
    }
}
