package com.ang.acb.bakeit.utils;

import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class RecipeIdlingResource implements IdlingResource {

    private final AtomicBoolean isIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(IdlingResource.ResourceCallback callback) {}
}

