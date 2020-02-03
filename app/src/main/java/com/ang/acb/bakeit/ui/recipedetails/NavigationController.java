package com.ang.acb.bakeit.ui.recipedetails;

import androidx.fragment.app.FragmentManager;

import com.ang.acb.bakeit.R;

import javax.inject.Inject;


/**
 * A utility class that handles navigation in DetailsActivity.
 *
 * See: https://github.com/android/architecture-components-samples/blob/7f3179f6599e15d4a21e8406c047bfe1e277cf69/GithubBrowserSample/app/src/main/java/com/android/example/github/ui/common/NavigationController.java
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
        fragmentManager.beginTransaction()
                .replace(recipeDetailsContainerId,
                        RecipeDetailsFragment.newInstance(recipeId, isTwoPane))
                .commit();
    }

    public void navigateToStepDetails(Integer recipeId, int stepPosition, boolean isTwoPane) {
        if (isTwoPane) {
            // Add StepDetailsFragment to its own separate fragment container.
            fragmentManager.beginTransaction()
                    .replace(stepsDetailsContainerId,
                            StepDetailsFragment.newInstance(recipeId, stepPosition, true))
                    .commit();
        } else {
            // Replace RecipeDetailsFragment with StepDetailsFragment
            // using the same fragment container.
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(recipeDetailsContainerId,
                            StepDetailsFragment.newInstance(recipeId, stepPosition, false))
                    .commit();
        }
    }
}
