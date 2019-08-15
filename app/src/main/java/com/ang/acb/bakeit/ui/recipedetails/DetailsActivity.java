package com.ang.acb.bakeit.ui.recipedetails;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ang.acb.bakeit.R;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.ARG_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

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
            isTwoPane = findViewById(R.id.step_video_fragment_container) != null;

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.partial_details_fragment_container,
                        RecipeDetailsFragment.newInstance(recipeId))
                .commit();
            Timber.d("Replace RecipeDetailsFragment in activity.");
        }
    }

}
