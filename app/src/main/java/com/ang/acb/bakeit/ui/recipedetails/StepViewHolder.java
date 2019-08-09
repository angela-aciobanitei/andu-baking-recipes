package com.ang.acb.bakeit.ui.recipedetails;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.StepItemBinding;

public class StepViewHolder extends RecyclerView.ViewHolder {

    private StepItemBinding binding;

    public StepViewHolder(@NonNull StepItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static StepViewHolder create (ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        StepItemBinding binding = StepItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new StepViewHolder(binding);
    }

    public void bindTo(Step step) {
        binding.stepItemCount.setText("*");
        binding.stepItemShortDescription.setText(step.getShortDescription());

        // Binding must be executed immediately. To force execution, use executePendingBindings().
        // https://developer.android.com/topic/libraries/data-binding/generated-binding#immediate_binding
        binding.executePendingBindings();
    }
}
