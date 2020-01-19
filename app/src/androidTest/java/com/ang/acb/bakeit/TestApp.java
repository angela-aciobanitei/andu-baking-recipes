package com.ang.acb.bakeit;

import android.app.Application;

/**
 * Use a separate App for tests to prevent initializing dependency injection.
 *
 * See {@link com.ang.acb.bakeit.util.BakingTestRunner}.
 */
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
