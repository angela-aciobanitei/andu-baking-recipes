package com.ang.acb.bakeit.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.ui.recipelist.RecipeListActivity;

/**
 * The AppWidgetProvider class implementation. Defines the basic methods that allow
 * you to programmatically interface with the App Widget, based on broadcast events.
 * Through it, you will receive broadcasts when the App Widget is updated, enabled,
 * disabled and deleted.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#AppWidgetProvider
 */
public class RecipeWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateRecipeWidget(context, appWidgetManager, appWidgetIds);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        // Update each of the app widgets with the remote adapter.
        for (int appWidgetId : appWidgetIds) {
            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews remoteViews = new RemoteViews(
                    context.getPackageName(), R.layout.widget_recipe);

            // Set widget title.
            remoteViews.setTextViewText(R.id.widget_ingredients_list_title,
                    PreferencesUtils.getWidgetTitle(context));

            // Set up the RemoteViews object to use a RemoteViews adapter. This
            // adapter connects to a RemoteViewsService  through the specified
            // intent. This is how we populate the data.
            remoteViews.setRemoteAdapter(R.id.widget_ingredients_list_items,
                    RecipeRemoteViewsService.getIntent(context));

            // Create an pending intent to launch RecipeListActivity.
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    new Intent(context, RecipeListActivity.class), 0);

            // When using collections (eg. ListView, StackView etc.) in widgets,
            // it is very costly to set PendingIntents on the individual items,
            // and is hence not permitted. Instead this method should be used
            // to set a single PendingIntent template on the collection, and
            // individual items can differentiate their on-click behavior using
            // RemoteViews#setOnClickFillInIntent(int, Intent).
            // See: https://developer.android.com/guide/topics/appwidgets#setting-up-the-pending-intent-template
            remoteViews.setPendingIntentTemplate(
                    R.id.widget_ingredients_list_items, pendingIntent);

            // Equivalent of calling View.setOnClickListener(android.view.View.OnClickListener)
            // to launch the provided RemoteResponse.
            remoteViews.setOnClickPendingIntent(
                    R.id.widget_cake_layout, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget.
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
