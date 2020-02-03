package com.ang.acb.bakeit.util;

import androidx.arch.core.executor.testing.CountingTaskExecutorRule;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;

import org.junit.runner.Description;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A Junit rule that registers Architecture Components' background threads
 * as an Espresso idling resource. Note: CountingTaskExecutorRule	is a JUnit
 * Test Rule that swaps the background executor used by the Architecture Components
 * with a different one which counts the tasks as they are start and finish.
 */
public class TaskExecutorWithIdlingResourceRule extends CountingTaskExecutorRule {

    private CopyOnWriteArrayList<IdlingResource.ResourceCallback> callbacks =
            new CopyOnWriteArrayList<>();

    @Override
    protected void starting(Description description) {
        IdlingRegistry.getInstance().register(new IdlingResource() {
            @Override
            public String getName() {
                return "Architecture Components Idling Resource";
            }

            @Override
            public boolean isIdleNow() {
                return TaskExecutorWithIdlingResourceRule.this.isIdle();
            }

            @Override
            public void registerIdleTransitionCallback(ResourceCallback callback) {
                callbacks.add(callback);
            }
        });
        super.starting(description);
    }

    @Override
    protected void onIdle() {
        super.onIdle();
        for (IdlingResource.ResourceCallback callback : callbacks) {
            callback.onTransitionToIdle();
        }
    }
}
