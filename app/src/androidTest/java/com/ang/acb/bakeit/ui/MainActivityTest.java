package com.ang.acb.bakeit.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.ui.recipelist.MainActivity;
import com.ang.acb.bakeit.ui.recipelist.RecipeListViewModel;
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

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Rule
    public TaskExecutorWithIdlingResourceRule executorRule =
            new TaskExecutorWithIdlingResourceRule();

    private MutableLiveData<Resource<List<Recipe>>> recipes = new MutableLiveData<>();

    @Before
    public void init() {
        EspressoTestUtil.disableAnimations(activityRule);

        RecipeListViewModel viewModel = Mockito.mock(RecipeListViewModel.class);

        // Fixed: error: no suitable method found for thenReturn()
        // when(viewModel.getLiveRecipes()).thenReturn(recipes);
        // See: http://not4j.com/mocking-method-with-generic-return-type/
        Mockito.doReturn(recipes).when(viewModel).getLiveRecipes();
        activityRule.getActivity().viewModelFactory = ViewModelUtil.createFor(viewModel);
    }


    @Test
    public void testRecipesLoading() {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.loading(data));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void testRecipesLoaded() throws InterruptedException {
        List<Recipe> data = TestUtil.createRecipeList();
        recipes.postValue(Resource.success(data));

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.retry_button)).check(matches(not(isDisplayed())));

        onView(withId(R.id.rv_recipe_list)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_list)).check(matches(hasMinimumChildCount(1)));

        onView(recipeListMatcher().atPosition(0))
                .check(matches(hasDescendant(withText("Nutella Pie"))));
        onView(recipeListMatcher().atPosition(1))
                .check(matches(hasDescendant(withText("Brownies"))));
        onView(recipeListMatcher().atPosition(2))
                .check(matches(hasDescendant(withText("Yellow Cake"))));
        onView(recipeListMatcher().atPosition(3))
                .check(matches(hasDescendant(withText("Cheesecake"))));
    }

    @Test
    public void testRecipeClickNavigatesToRecipeDetails() {
        onView(recipeListMatcher().atPosition(0)).perform(click());

        onView(withId(R.id.partial_details_fragment_container))
                .check(matches(isDisplayed()));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));
    }

    @NonNull
    private RecyclerViewMatcher recipeListMatcher() {
        return new RecyclerViewMatcher(R.id.rv_recipe_list);
    }
}
