package com.ang.acb.bakeit.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.ui.recipedetails.NavigationController;
import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;
import com.ang.acb.bakeit.ui.recipedetails.DetailsViewModel;
import com.ang.acb.bakeit.ui.recipedetails.RecipeDetailsFragment;
import com.ang.acb.bakeit.util.EspressoTestUtil;
import com.ang.acb.bakeit.util.RecyclerViewMatcher;
import com.ang.acb.bakeit.util.TaskExecutorWithIdlingResourceRule;
import com.ang.acb.bakeit.util.TestUtil;
import com.ang.acb.bakeit.util.ViewModelUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailsFragmentTest {

    @Rule
    public ActivityTestRule<DetailsActivity> activityRule =
            new ActivityTestRule<>(DetailsActivity.class);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private RecipeDetailsFragment detailsFragment;
    private NavigationController navigationController;
    private DetailsViewModel viewModel;
    private MutableLiveData<RecipeDetails> recipeDetails = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableAnimations(activityRule);

        detailsFragment = RecipeDetailsFragment.newInstance(0, false);
        viewModel = Mockito.mock(DetailsViewModel.class);
        navigationController = mock(NavigationController.class);

        when(viewModel.getRecipeDetailsLiveData()).thenReturn(recipeDetails);

        detailsFragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        detailsFragment.navigationController = navigationController;

        activityRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.partial_details_fragment_container,
                        detailsFragment, "TEST")
                .commit();
    }

    @Test
    public void listsAreDisplayed() throws InterruptedException {
        recipeDetails.postValue(TestUtil.createDetailedRecipe());
        onView(withId(R.id.rv_ingredients)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_ingredients)).check(matches(hasMinimumChildCount(1)));
        onView(withId(R.id.rv_steps)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_steps)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testIngredients() {
        onView(ingredientsListMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(ingredientsListMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));
    }

    @Test
    public void testStepClick() {
        onView(stepsListMatcher().atPosition(0)).perform(click());
        verify(navigationController).navigateToStepDetails(0, 0, false);
    }


    @NonNull
    private RecyclerViewMatcher ingredientsListMatcher() {
        return new RecyclerViewMatcher(R.id.rv_ingredients);
    }

    @NonNull
    private RecyclerViewMatcher stepsListMatcher() {
        return new RecyclerViewMatcher(R.id.rv_steps);
    }
}