package com.ang.acb.bakeit.util;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

import com.ang.acb.bakeit.TestApp;

/**
 * Custom runner to disable dependency injection.
 */
public class BakingTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader classLoader, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {

        return super.newApplication(classLoader, TestApp.class.getName(), context);
    }
}
