package com.ang.acb.bakeit.ui.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.StepItemBinding;

import java.util.List;


public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private StepClickCallback clickCallback;
    private List<Step> stepList;

    public StepAdapter (StepClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and obtain an instance of the binding class.
        StepItemBinding binding = StepItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        return new StepItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind step data.
        Step step = stepList.get(position);
        ((StepItemViewHolder) holder).bindTo(step);

        // Handle step item click events.
        holder.itemView.setOnClickListener(v -> {
            if (step != null && clickCallback != null) {
                clickCallback.onClick(position);
            }
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

    public interface StepClickCallback {
        void onClick(int position);
    }

    class StepItemViewHolder extends RecyclerView.ViewHolder {

        private StepItemBinding binding;

        StepItemViewHolder(@NonNull StepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Step step) {
            // Bind step data.
            binding.setStep(step);
            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }
}
