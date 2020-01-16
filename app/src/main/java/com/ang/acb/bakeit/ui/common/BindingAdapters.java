package com.ang.acb.bakeit.ui.common;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.ui.recipedetails.IngredientAdapter;
import com.ang.acb.bakeit.ui.recipedetails.StepAdapter;

import java.util.List;

import timber.log.Timber;

/**
 * Binding adapters are responsible for making the appropriate framework calls to set values.
 *
 * See: https://developer.android.com/topic/libraries/data-binding/binding-adapters
 * See: https://github.com/android/android-sunflower
 */
public class BindingAdapters {

    @BindingAdapter("toggleVisibility")
    public static void toggleVisibility(View view, Boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("ingredientItems")
    public static void setIngredientItems(RecyclerView recyclerView, List<Ingredient> items) {
        IngredientAdapter adapter = (IngredientAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.submitList(items);
        Timber.d("setIngredientItems");
    }

    @BindingAdapter("stepItems")
    public static void setStepItems(RecyclerView recyclerView, List<Step> items) {
        StepAdapter adapter = (StepAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.submitList(items);
        Timber.d("setStepItems");
    }
}
