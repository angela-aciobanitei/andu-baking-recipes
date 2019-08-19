package com.ang.acb.bakeit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * A service to be connected to for a remote adapter to request RemoteViews.
 * It provides the RemoteViewsFactory used to populate the remote collection view.
 * RemoteViewsFactory is an interface for an adapter between a remote collection
 * view (such as ListView, GridView) and the underlying data for that view.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsservice-class
 */
public class RecipeRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(getApplicationContext());
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, RecipeRemoteViewsService.class);
    }
}