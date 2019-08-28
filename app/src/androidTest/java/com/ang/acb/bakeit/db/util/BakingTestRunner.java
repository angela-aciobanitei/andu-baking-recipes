package com.ang.acb.bakeit.db.util;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

import com.ang.acb.bakeit.db.TestApp;

/**
 * Custom runner to disable dependency injection.
 */
public class BakingTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(cl, TestApp.class.getName(), context);
    }
}
