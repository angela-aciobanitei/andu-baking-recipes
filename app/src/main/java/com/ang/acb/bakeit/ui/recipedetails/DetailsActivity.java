package com.ang.acb.bakeit.ui.recipedetails;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.ui.widget.PreferencesUtils;
import com.ang.acb.bakeit.ui.widget.RecipeWidgetProvider;


import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListFragment.EXTRA_RECIPE_ID;
import static com.ang.acb.bakeit.ui.recipelist.RecipeListFragment.EXTRA_RECIPE_NAME;
import static com.ang.acb.bakeit.ui.recipelist.RecipeListFragment.INVALID_RECIPE_ID;

public class DetailsActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static final String EXTRA_IS_TWO_PANE = "EXTRA_IS_TWO_PANE";
    public static final String EXTRA_STEP_POSITION = "EXTRA_STEP_POSITION";

    private Integer recipeId;
    private String recipeName;

    @Inject
    NavigationController navigationController;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // When using Dagger with activities, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getIntentExtras();
        setupToolbar();

        if (savedInstanceState == null) {
            // Display only recipe details (ingredients list ans step list) on phones.
            navigationController.navigateToRecipeDetails(recipeId, isTwoPane());
            // Display both recipe details and step details on tablets.
            if (isTwoPane()) {
                navigationController.navigateToStepDetails(recipeId, 0, isTwoPane());
            }
        }
    }

    private void getIntentExtras() {
        recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID);
        recipeName = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        Timber.d("Recipe ID: %s.", recipeId);
        if (recipeId.equals(INVALID_RECIPE_ID)) closeOnError();
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipeName);
        }
    }

    public boolean isTwoPane() {
        return findViewById(R.id.step_details_fragment_container) != null;
    }

    private void closeOnError() {
        finish();
        Toast.makeText(
                this,
                R.string.detail_error_message,
                Toast.LENGTH_SHORT)
                .show();
    }

    public void addWidgetToHomeScreen(Integer recipeId, String recipeName) {
        PreferencesUtils.setWidgetTitle(this, recipeName);
        PreferencesUtils.setWidgetRecipeId(this, recipeId);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetIds, R.id.widget_ingredients_list_items);
        RecipeWidgetProvider.updateRecipeWidget(
                this, appWidgetManager, appWidgetIds);
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
