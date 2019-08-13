package com.ang.acb.bakeit.data.model;

import androidx.core.util.Pair;
import androidx.room.Relation;

import java.util.List;

public class Details {

    private Pair<List<Ingredient>, List<Step>> detailsPair;

    public Details(Pair<List<Ingredient>, List<Step>> detailsPair) {
        this.detailsPair = detailsPair;
    }

    // Note: we need these getters for the data binding.
    public List<Ingredient> getIngredients () {
        return detailsPair.first;
    }

    public List<Step> getSteps() {
        return detailsPair.second;
    }
}
