package com.ang.acb.bakeit.ui.common;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;
import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsFragment;
import com.ang.acb.bakeit.ui.recipedetails.StepDetailsFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * A utility class that handles navigation in DetailsActivity.
 */
public class NavigationController {

    private final int recipeDetailsContainerId;
    private final int stepsDetailsContainerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(DetailsActivity detailsActivity) {
        recipeDetailsContainerId = R.id.partial_details_fragment_container;
        stepsDetailsContainerId = R.id.step_details_fragment_container;
        fragmentManager = detailsActivity.getSupportFragmentManager();
    }

    public void navigateToRecipeDetails(Integer recipeId, boolean isTwoPane) {
        fragmentManager
                .beginTransaction()
                .replace(recipeDetailsContainerId,
                        RecipeDetailsFragment.newInstance(recipeId, isTwoPane))
                .commit();
        Timber.d("Navigate to RecipeDetailsFragment.");
    }

    public void navigateToStepDetails(Integer recipeId, int stepPosition, boolean isTwoPane) {
        if (isTwoPane) {
            // Add StepDetailsFragment to its own separate fragment container.
            fragmentManager
                    .beginTransaction()
                    .replace(stepsDetailsContainerId,
                            StepDetailsFragment.newInstance(recipeId, stepPosition))
                    .commit();
        } else {
            // Replace RecipeDetailsFragment with StepDetailsFragment
            // using the same fragment container.
            fragmentManager
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(recipeDetailsContainerId,
                            StepDetailsFragment.newInstance(recipeId, stepPosition))
                    .commit();
        }
        Timber.d("Navigate to StepDetailsFragment.");
    }
}
