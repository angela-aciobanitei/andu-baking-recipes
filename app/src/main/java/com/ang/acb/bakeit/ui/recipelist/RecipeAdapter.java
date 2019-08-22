package com.ang.acb.bakeit.ui.recipelist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecipeListViewModel viewModel;
    private Resource<List<Recipe>> resourceRecipeList;
    private Resource networkState = null;

    @Inject
    public RecipeAdapter (RecipeListViewModel viewModel) {
        this.viewModel = viewModel;
        resourceRecipeList = viewModel.getRecipeListResourceLiveData().getValue();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case R.layout.recipe_item:
                return RecipeItemViewHolder.create(parent);
            case R.layout.network_state_item:
                return NetworkStateItemViewHolder.create(parent, viewModel);
            default:
                throw new IllegalArgumentException("Unknown view type " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case R.layout.recipe_item:
                ((RecipeItemViewHolder) holder).bindTo(
                        resourceRecipeList.getData().get(position));
                break;
            case R.layout.network_state_item:
                ((NetworkStateItemViewHolder) holder).bindTo(networkState);
                break;
            default:
                throw new IllegalArgumentException(
                        "Unknown view type " + getItemViewType(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.recipe_item;
        }
    }

    @Override
    public int getItemCount() {
        int listSize = resourceRecipeList.getData() == null ? 0 :  resourceRecipeList.getData().size();
        return listSize + (hasExtraRow() ? 1 : 0);
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState.status != Resource.Status.SUCCESS;
    }

    public void submitList(Resource<List<Recipe>> recipes) {
        resourceRecipeList = recipes;
        notifyDataSetChanged();
    }

    public void setNetworkState(Resource currentNetworkState) {
        Resource previousNetworkState = networkState;
        boolean hadExtraRow = hasExtraRow();
        networkState = currentNetworkState;
        boolean hasExtraRow = hasExtraRow();
        int listSize = resourceRecipeList.getData() == null ? 0 :  resourceRecipeList.getData().size();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(listSize);
            } else {
                notifyItemInserted(listSize);
            }
        } else if (hasExtraRow && !previousNetworkState.equals(currentNetworkState)) {
            notifyItemChanged(getItemCount() - 1);
        }
    }
}
