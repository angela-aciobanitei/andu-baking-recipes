package com.ang.acb.bakeit.utils;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;

import java.util.List;

/**
 * A custom MediatorLiveData that combines a live data Recipe object and a live data list of ingredients.
 * See: https://stackoverflow.com/questions/49493772/mediatorlivedata-or-switchmap-transformation-with-multiple-parameters
 */

public class DetailsLiveData extends MediatorLiveData<Pair<List<Ingredient>, List<Step>>> {

    public DetailsLiveData(LiveData<List<Ingredient>> ingredients, LiveData<List<Step>> steps) {

        addSource(ingredients, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                setValue(Pair.create(ingredients, steps.getValue()));
            }
        });

        addSource(steps, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                setValue(Pair.create(ingredients.getValue(), steps));
            }
        });
    }
}
