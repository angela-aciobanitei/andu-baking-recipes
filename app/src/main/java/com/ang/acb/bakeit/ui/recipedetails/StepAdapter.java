package com.ang.acb.bakeit.ui.recipedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

import javax.inject.Inject;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> stepList;
    private RecipeDetailsViewModel viewModel;

    @Inject
    public StepAdapter(RecipeDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StepItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind step data
        Step step = stepList.get(position);
        ((StepItemViewHolder) holder).bindTo(step);
        // Handle step click events
        holder.itemView.setOnClickListener(view -> {
            // Update the current step position.
            viewModel.setStepIndex(position);
            viewModel.setOpenStepDetailsEvent(position);
        });
    }

    @Override
    public int getItemCount() {
        return stepList == null ? 0 : stepList.size();
    }

    public void submitList(List<Step> steps) {
        stepList = steps;
        notifyDataSetChanged();
    }
}
