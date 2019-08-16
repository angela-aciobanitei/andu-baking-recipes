package com.ang.acb.bakeit.ui.recipedetails;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.ARG_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

    private RecipeDetailsViewModel viewModel;
    private boolean isTwoPane;
    private Integer recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recipeId = getIntent().getIntExtra(ARG_RECIPE_ID, INVALID_RECIPE_ID);
        Timber.d("Recipe ID: %s.", recipeId);
        if (recipeId.equals(INVALID_RECIPE_ID)) {
            Timber.d("Invalid recipe id.");
            return;
        }

        if (savedInstanceState == null) {
            // TODO Handle two pane layouts
            isTwoPane = findViewById(R.id.step_details_fragment_container) != null;

            // Init view model
            ViewModelFactory factory = InjectorUtils.provideViewModelFactory(this);
            viewModel = ViewModelProviders.of(this,factory)
                    .get(RecipeDetailsViewModel.class);

            // TODO Observe steps list click event
            // mViewModel.getOpenStepDetailEvent().observe(this, new Observer<Integer>() {});

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.partial_details_fragment_container,
                        RecipeDetailsFragment.newInstance(recipeId))
                .commit();
            Timber.d("Replace RecipeDetailsFragment in activity.");

            if (isTwoPane) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.step_details_fragment_container,
                                StepDetailsFragment.newInstance(recipeId))
                        .commit();
            }
        }
    }

}
