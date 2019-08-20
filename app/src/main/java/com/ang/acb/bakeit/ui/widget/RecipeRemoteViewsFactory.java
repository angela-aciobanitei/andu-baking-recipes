package com.ang.acb.bakeit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * A custom class that implements the RemoteViewsFactory interface and provides
 * the app widget with the data for the items in its collection.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsfactory-interface
 */
public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private AppDatabase database;
    private List<String> ingredients;

    RecipeRemoteViewsFactory(Context context) {
        this.context = context;
        // FIXME: Use repo
        database = Room.databaseBuilder(context, AppDatabase.class, "recipes.db")
                .fallbackToDestructiveMigration()
                .build();;
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        int recipeId = PreferencesUtils.getWidgetRecipeId(context);

        if (recipeId != -1) {
            ingredients = new ArrayList<>();
            WholeRecipe wholeRecipe = database.recipeDao().loadRecipe(recipeId);
            if (wholeRecipe != null) {
                for (Ingredient ingredient : wholeRecipe.ingredients) {
                    ingredients.add(String.format(
                            Locale.getDefault(),
                            "%.1f %s %s",
                            ingredient.getQuantity(),
                            ingredient.getMeasure(),
                            ingredient.getIngredient()));
                }
            }
        }
    }

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return ingredients == null ? 0 : ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION || ingredients == null) {
            return null;
        }

        // Construct a remote views item based on the app widget item XML file.
        RemoteViews remoteViews =  new RemoteViews(
                context.getPackageName(),
                R.layout.widget_ingredient_item);

        // Set the remote views item text based on the position.
        remoteViews.setTextViewText(
                R.id.widget_ingredient_item,
                ingredients.get(position));

        // When using collections (eg. ListView, StackView etc.) in widgets,
        // it is very costly to set PendingIntents on the individual items,
        // and is hence not permitted. Instead this method should be used
        // to set a single PendingIntent template on the collection, and
        // individual items can differentiate their on-click behavior using
        // RemoteViews#setOnClickFillInIntent(int, Intent).
        // See: https://developer.android.com/guide/topics/appwidgets#setting-the-fill-in-intent
        remoteViews.setOnClickFillInIntent(
                R.id.widget_ingredient_item,
                new Intent());

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

