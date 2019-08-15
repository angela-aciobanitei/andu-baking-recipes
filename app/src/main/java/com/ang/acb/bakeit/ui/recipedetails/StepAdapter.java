package com.ang.acb.bakeit.ui.recipedetails;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

import timber.log.Timber;

public class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Step> stepList;
    private Context context;
    private Integer recipeId;

    public StepAdapter(Context context, Integer recipeId) {
        this.context = context;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return StepItemViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Step step = stepList.get(position);
        ((StepItemViewHolder) holder).bindTo(step);
        ((StepItemViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FIXME: Find a better way to do this
                ((AppCompatActivity) context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.partial_details_fragment_container,
                                StepVideoFragment.newInstance(recipeId, step.getId()))
                        .addToBackStack(String.valueOf(R.string.app_name))
                        .commit();
                Timber.d("Replace StepVideoFragment in activity.");
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
}
