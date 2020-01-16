package com.ang.acb.bakeit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.local.RecipeDao;
import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.RecipeDetails;

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

    @Inject
    RecipeDao recipeDao;
    private Context context;
    private List<String> ingredients;

    @Inject
    RecipeRemoteViewsFactory(Context context, RecipeDao recipeDao) {
        this.context = context;
        this.recipeDao = recipeDao;
    }

    @Override
    public void onCreate() {}


    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter.
     * This allows a RemoteViewsFactory to respond to data changes by updating
     * any internal references. Note: expensive tasks can be safely performed
     * synchronously within this method. In the interim, the old data will be
     * displayed within the widget.
     */
    @Override
    public void onDataSetChanged() {
        int recipeId = PreferencesUtils.getWidgetRecipeId(context);

        if (recipeId != -1) {
            ingredients = new ArrayList<>();
            RecipeDetails recipeDetails = recipeDao.getRecipeDetailsSync(recipeId);
            if (recipeDetails != null) {
                for (Ingredient ingredient : recipeDetails.ingredients) {
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
        remoteViews.setOnClickFillInIntent(R.id.widget_ingredient_item, new Intent());

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

