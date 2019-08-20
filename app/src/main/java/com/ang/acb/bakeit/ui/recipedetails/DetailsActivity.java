package com.ang.acb.bakeit.ui.recipedetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.ui.widget.PreferencesUtils;
import com.ang.acb.bakeit.ui.widget.RecipeWidgetProvider;


import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_NAME;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity
                             implements HasSupportFragmentInjector {

    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;
    private String recipeName;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getIntentExtras();
        createViewModel();

        if (savedInstanceState == null) {
            viewModel.init(recipeId);
            addRecipeDetailsFragment();
        }

        // Observe steps list click event
        viewModel.getOpenStepDetailsEvent().observe(this, stepPosition -> {
            if (!isTwoPane()) replaceRecipeDetailsFragment();
            else addStepDetailsFragment();
        });
    }

    private void getIntentExtras() {
        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
        recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        Timber.d("Recipe ID: %s.", recipeId);
        if (recipeId.equals(INVALID_RECIPE_ID)) Timber.d("Invalid recipe id.");

    }

    private void createViewModel(){
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeDetailsViewModel.class);
    }

    private boolean isTwoPane() {
        return findViewById(R.id.step_details_fragment_container) != null;
    }

    private void addRecipeDetailsFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.partial_details_fragment_container,
                        RecipeDetailsFragment.newInstance(recipeId))
                .commit();
        Timber.d("Add RecipeDetailsFragment to its fragment container.");
    }

    private void replaceRecipeDetailsFragment() {
        // Replace RecipeDetailsFragment with StepDetailsFragment
        // using the same fragment container.
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(getString(R.string.add_to_back_stack_key))
                .replace(R.id.partial_details_fragment_container,
                        StepDetailsFragment.newInstance(recipeId))
                .commit();
        Timber.d("Add StepDetailsFragment in the same fragment container.");
    }

    private void addStepDetailsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.step_details_fragment_container,
                        StepDetailsFragment.newInstance(recipeId))
                .commit();
        Timber.d("Add StepDetailsFragment to its own separate fragment container.");
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
