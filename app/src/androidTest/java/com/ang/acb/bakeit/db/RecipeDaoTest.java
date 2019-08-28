package com.ang.acb.bakeit.db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.util.LiveDataTestUtil;
import com.ang.acb.bakeit.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest extends DbTest {

    @Rule
    // See: https://stackoverflow.com/questions/52274924/cannot-invoke-observeforever-on-a-background-thread
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void insertSimpleRecipe() throws InterruptedException {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe("Nutella Pie", 8);
        db.recipeDao().insertRecipe(simpleRecipe);
        Recipe loaded = LiveDataTestUtil.getValue(db.recipeDao().loadSimpleRecipe(Recipe.UNKNOWN_ID));
        assertThat(loaded, notNullValue());
        assertThat(loaded.getName(), is("Nutella Pie"));
        assertThat(loaded.getServings(), is(8));
        assertThat(loaded.getSteps(), nullValue());
        assertThat(loaded.getIngredients(), nullValue());
    }
}
