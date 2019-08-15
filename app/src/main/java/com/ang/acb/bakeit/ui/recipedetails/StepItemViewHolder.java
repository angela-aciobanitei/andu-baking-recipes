package com.ang.acb.bakeit.ui.recipedetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.StepItemBinding;

public class StepItemViewHolder extends RecyclerView.ViewHolder {

    private StepItemBinding binding;
    private Context context;

    public StepItemViewHolder(@NonNull StepItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public static StepItemViewHolder create (ViewGroup parent) {
        // Inflate view and obtain an instance of the binding class.
        StepItemBinding binding = StepItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new StepItemViewHolder(binding);
    }

    public void bindTo(Step step) {
        binding.setStep(step);

        // Binding must be executed immediately.
        binding.executePendingBindings();
    }
}
