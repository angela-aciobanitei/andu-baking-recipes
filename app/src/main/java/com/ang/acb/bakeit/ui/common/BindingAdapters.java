package com.ang.acb.bakeit.ui.common;

import android.view.View;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.ui.recipedetails.IngredientAdapter;
import com.ang.acb.bakeit.ui.recipedetails.StepAdapter;

import java.util.List;

/**
 * Binding adapters are responsible for making the appropriate framework calls to set values.
 *
 * See: https://developer.android.com/topic/libraries/data-binding/binding-adapters
 * See: https://github.com/googlesamples/android-sunflower/blob/master/app/src/main/java/com/google/samples/apps/sunflower/adapters
 */
public class BindingAdapters {

    @BindingAdapter("toggleVisibility")
    public static void toggleVisibility(View view, Boolean isVisible) {
        view.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("setIngredientItems")
    public static void setIngredientItems(RecyclerView recyclerView, List<Ingredient> items) {
        IngredientAdapter adapter = (IngredientAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.submitList(items);
    }

    @BindingAdapter("setStepItems")
    public static void setStepItems(RecyclerView recyclerView, List<Step> items) {
        StepAdapter adapter = (StepAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.submitList(items);
    }
}