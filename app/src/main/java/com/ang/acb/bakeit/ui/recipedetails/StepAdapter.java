package com.ang.acb.bakeit.ui.recipedetails;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> stepList;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StepItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Step step = stepList.get(position);
        ((StepItemViewHolder) holder).bindTo(step);
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
