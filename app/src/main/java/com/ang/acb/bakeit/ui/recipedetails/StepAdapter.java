package com.ang.acb.bakeit.ui.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.StepItemBinding;

import java.util.List;

import javax.inject.Inject;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface StepClickCallback {
        void onClick(Step step);
    }

    private StepClickCallback clickCallback;
    private List<Step> stepList;
    private RecipeDetailsViewModel viewModel;


    public StepAdapter(RecipeDetailsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public StepAdapter (StepClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    class StepItemViewHolder extends RecyclerView.ViewHolder {

        private StepItemBinding binding;

        StepItemViewHolder(@NonNull StepItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTo(Step step) {
            binding.setStep(step);

            // Binding must be executed immediately.
            binding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate view and obtain an instance of the binding class.
        StepItemBinding binding = StepItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);

        binding.getRoot().setOnClickListener(v -> {
            Step step = binding.getStep();
            if (step != null && clickCallback != null) {
                clickCallback.onClick(step);
            }
        });
        return new StepItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // Bind step data
        Step step = stepList.get(position);
        ((StepItemViewHolder) holder).bindTo(step);
//        // Handle step click events
//        holder.itemView.setOnClickListener(view -> {
//            // Update the current step position.
//            viewModel.setStepIndex(position);
//            viewModel.setOpenStepDetailsEvent(position);
//        });
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
