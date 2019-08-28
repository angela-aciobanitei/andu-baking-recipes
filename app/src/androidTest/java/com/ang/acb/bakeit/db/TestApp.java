package com.ang.acb.bakeit.db;

import android.app.Application;

/**
 * Use a separate App for tests to prevent initializing dependency injection.
 */
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
