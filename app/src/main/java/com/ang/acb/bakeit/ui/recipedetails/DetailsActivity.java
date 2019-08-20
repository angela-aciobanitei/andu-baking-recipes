package com.ang.acb.bakeit.ui.recipedetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.ui.widget.PreferencesUtils;
import com.ang.acb.bakeit.ui.widget.RecipeWidgetProvider;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;


import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_NAME;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity {

    private RecipeDetailsViewModel viewModel;
    private boolean isTwoPane;
    private Integer recipeId;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
        recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        Timber.d("Recipe ID: %s.", recipeId);
        if (recipeId.equals(INVALID_RECIPE_ID)) {
            Timber.d("Invalid recipe id.");
            return;
        }

        isTwoPane = findViewById(R.id.step_details_fragment_container) != null;

        // Create view model
        viewModel = obtainViewModel(this);

        if (savedInstanceState == null) {
            viewModel.init(recipeId);
            addWidgetToHomeScreen(recipeId, recipeName);
            // Add RecipeDetailsFragment to its fragment container
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.partial_details_fragment_container,
                            RecipeDetailsFragment.newInstance(recipeId))
                    .commit();
            Timber.d("Add RecipeDetailsFragment to its fragment container.");
        }

        // TODO Observe steps list click event
        viewModel.getOpenStepDetailsEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer stepPosition) {
                if (!isTwoPane) {
                    // Replace RecipeDetailsFragment with StepDetailsFragment
                    // using the same fragment container
                    getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(getString(R.string.add_to_back_stack_key))
                            .replace(R.id.partial_details_fragment_container,
                                    StepDetailsFragment.newInstance(recipeId))
                            .commit();
                    Timber.d("Add StepDetailsFragment in the same fragment container.");
                } else {
                    // Add StepDetailsFragment to its own separate fragment container
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.step_details_fragment_container,
                                    StepDetailsFragment.newInstance(recipeId))
                            .commit();
                    Timber.d("Add StepDetailsFragment to its own separate fragment container.");
                }
            }
        });
    }

    public static RecipeDetailsViewModel obtainViewModel(FragmentActivity activity) {
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(activity);
        return ViewModelProviders.of(activity, factory).get(RecipeDetailsViewModel.class);
    }

    public void addWidgetToHomeScreen(Integer recipeId, String recipeName) {
        PreferencesUtils.setWidgetTitle(this, recipeName);
        PreferencesUtils.setWidgetRecipeId(this, recipeId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_ingredients_list_items);
        RecipeWidgetProvider.updateRecipeWidget(this, appWidgetManager, appWidgetIds);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create_widget) {
            addWidgetToHomeScreen(recipeId, recipeName);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
