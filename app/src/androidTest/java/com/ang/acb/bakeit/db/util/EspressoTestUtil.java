package com.ang.acb.bakeit.db.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.rule.ActivityTestRule;

import org.jetbrains.annotations.NotNull;

/**
 * Utility methods for espresso tests.
 */
public class EspressoTestUtil {
    /**
     * Disables progress bar animations for the views of the given activity rule
     *
     * @param activityTestRule The activity rule whose views will be checked
     */
    public static void disableProgressBarAnimations(
            ActivityTestRule<? extends FragmentActivity> activityTestRule) {
        activityTestRule.getActivity().getSupportFragmentManager()
                .registerFragmentLifecycleCallbacks(
                        new FragmentManager.FragmentLifecycleCallbacks() {
                            @Override
                            public void onFragmentViewCreated(@NotNull FragmentManager fm,
                                                              @NotNull Fragment f, @NotNull View v,
                                                              Bundle savedInstanceState) {
                                // Traverse all views, if any is a progress bar,
                                // replace its animation.
                                traverseViews(v);
                            }
                        }, true);
    }

    private static void traverseViews(View view) {
        if (view instanceof ViewGroup) {
            traverseViewGroup((ViewGroup) view);
        } else {
            if (view instanceof ProgressBar) {
                disableProgressBarAnimation((ProgressBar) view);
            }
        }
    }

    private static void traverseViewGroup(ViewGroup view) {
        final int count = view.getChildCount();
        for (int i = 0; i < count; i++) {
            traverseViews(view.getChildAt(i));
        }
    }

    /**
     * Necessary to run tests on older API levels where progress bar uses handler loop to animate.
     * @param progressBar The progress bar whose animation will be swapped with a drawable
     */
    private static void disableProgressBarAnimation(ProgressBar progressBar) {
        progressBar.setIndeterminateDrawable(new ColorDrawable(Color.BLUE));
    }
}
