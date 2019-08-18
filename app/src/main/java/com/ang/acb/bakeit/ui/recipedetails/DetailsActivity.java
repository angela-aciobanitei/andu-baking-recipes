package com.ang.acb.bakeit.ui.recipedetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

    private RecipeDetailsViewModel viewModel;
    private boolean isTwoPane;
    private Integer recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
        Timber.d("Recipe ID: %s.", recipeId);
        if (recipeId.equals(INVALID_RECIPE_ID)) {
            Timber.d("Invalid recipe id.");
            return;
        }

        // TODO Handle two pane layouts
        isTwoPane = findViewById(R.id.step_details_fragment_container) != null;

        // Create view model
        viewModel = obtainViewModel(this);

        if (savedInstanceState == null) {
            viewModel.init(recipeId, isTwoPane);
            // Add only RecipeDetailsFragment to its fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.partial_details_fragment_container,
                            RecipeDetailsFragment.newInstance(recipeId))
                    .commit();
            Timber.d("Replace RecipeDetailsFragment in activity.");
        }

        // FIXME Observe steps list click event
        viewModel.getCurrentPositionLiveEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer stepPosition) {
                if (!isTwoPane) {
                    // Replace RecipeDetailsFragment with StepDetailsFragment
                    // using the same fragment container
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.partial_details_fragment_container,
                                    StepDetailsFragment.newInstance(recipeId, stepPosition))
                            .commit();
                } else {
                    // Add StepDetailsFragment to its own separate fragment container
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.step_details_fragment_container,
                                    StepDetailsFragment.newInstance(recipeId, stepPosition))
                            .commit();
                }
            }
        });
    }

    public static RecipeDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(RecipeDetailsViewModel.class);
    }

}
