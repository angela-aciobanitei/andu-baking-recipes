package com.ang.acb.bakeit.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.ui.recipedetails.DetailsActivity;
import com.ang.acb.bakeit.ui.recipelist.MainActivity;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;
import com.ang.acb.bakeit.util.EspressoTestUtil;
import com.ang.acb.bakeit.util.RecyclerViewMatcher;
import com.ang.acb.bakeit.util.TaskExecutorWithIdlingResourceRule;
import com.ang.acb.bakeit.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    // FIXME: This live data value is not updated
    private MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();
    private RecipeListViewModel viewModel;

    @Before
    public void init() {
        EspressoTestUtil.disableAnimations(activityRule);
        Intents.init();

        viewModel = Mockito.mock(RecipeListViewModel.class);
        when(viewModel.getLiveRecipes()).thenReturn(recipes);
    }

    @Test
    public void testLoading() {
        recipes.postValue(Resource.loading(null));
        // FIXME: Expected: is displayed on the screen to the user, Got: ProgressBar
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testValueWhileLoading() {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.loading(data));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testLoaded() throws InterruptedException {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.success(data));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rv_recipe_list)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_list)).check(matches(hasMinimumChildCount(1)));
    }

    @Test
    public void testError() throws InterruptedException {
        recipes.postValue(Resource.error("foo", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        // FIXME: Expected: is displayed on the screen to the user Got: MaterialButton
        onView(withId(R.id.retry_button)).check(matches(isDisplayed()));
        // FIXME: Error performing 'single click' on view with id:retry_button
        onView(withId(R.id.retry_button)).perform(click());

        verify(viewModel).retry();
        recipes.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));

        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.success(data));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.rv_recipe_list)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecipes() {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.success(data));

        onView(withId(R.id.rv_recipe_list))
                .check(matches(isDisplayed()));
        onView(listMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(listMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));
    }

    @Test
    public void testRecipeClick() {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.success(data));

        onView(withId(R.id.rv_recipe_list))
                .perform(actionOnItemAtPosition(0, click()));
        onView(withId(R.id.partial_details_fragment_container))
                .check(matches(isDisplayed()));
        intended(hasComponent(DetailsActivity.class.getName()));
    }

    @NonNull
    private RecyclerViewMatcher listMatcher() {
        return new RecyclerViewMatcher(R.id.rv_recipe_list);
    }


}
