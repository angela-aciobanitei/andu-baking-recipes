package com.ang.acb.bakeit.db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ang.acb.bakeit.data.local.AppDatabase;
import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.util.LiveDataTestUtil;
import com.ang.acb.bakeit.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ang.acb.bakeit.util.TestUtil.createIngredient;
import static com.ang.acb.bakeit.util.TestUtil.createStep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AppDatabase database;

    @Before
    public void initDb() {
        database = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                            .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void insertSimpleRecipeThenLoad() throws InterruptedException {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe(
                1, "Nutella Pie", 8, "");

        database.recipeDao().insertRecipe(simpleRecipe);

        Recipe loaded = LiveDataTestUtil.getValue(
                database.recipeDao().getRecipe(1));

        assertThat(loaded, notNullValue());
        assertThat(loaded.getName(), is("Nutella Pie"));
        assertThat(loaded.getServings(), is(8));
        assertThat(loaded.getSteps(), nullValue());
        assertThat(loaded.getIngredients(), nullValue());
    }

    @Test
    public void insertWholeRecipeThenLoad() throws InterruptedException {
        Recipe recipe = TestUtil.createSimpleRecipe(
                1, "Nutella Pie", 8, "");
        recipe.setIngredients(Arrays.asList(
                createIngredient(1,"CUP", "foo"),
                createIngredient(2,"TBLSP", "bar"),
                createIngredient(3,"K", "Nutella")
        ));
        recipe.setSteps(Arrays.asList(
                createStep(0, "foo","foo foo", "",""),
                createStep(1, "bar","bar bar", "",""),
                createStep(2, "moo","moo moo", "","")
        ));

        database.recipeDao().insertAllRecipes(Collections.singletonList(recipe));
        RecipeDetails recipeDetails = LiveDataTestUtil.getValue(database.recipeDao().getRecipeDetails(1));

        assertThat(recipeDetails, notNullValue());
        assertThat(recipeDetails.recipe.getName(), is("Nutella Pie"));
        assertThat(recipeDetails.recipe.getServings(), is(8));

        assertThat(recipeDetails.ingredients, notNullValue());
        assertThat(recipeDetails.ingredients.size(), is(3));

        assertThat(recipeDetails.steps, notNullValue());
        assertThat(recipeDetails.steps.size(), is(3));
    }

    @Test
    public void loadStepsAndIngredients() throws InterruptedException {
        Recipe cake = TestUtil.createSimpleRecipe(
                3, "Yellow Cake", 6,"");
        cake.setIngredients(Arrays.asList(
                createIngredient(400,"G", "flour"),
                createIngredient(700,"G", "sugar"),
                createIngredient(2,"TBS", "salt"),
                createIngredient(4,"TSP", "baking powder")
        ));
        cake.setSteps(Arrays.asList(
                createStep(0,"foo","foo foo", "",""),
                createStep(1, "coo","coo coo", "",""),
                createStep(2, "moo","moo moo", "","")
        ));

        database.recipeDao().insertAllRecipes(Collections.singletonList(cake));

        List<Ingredient> ingredients = LiveDataTestUtil.getValue(
                database.recipeDao().getRecipeIngredients(3));
        assertThat(ingredients, notNullValue());
        assertThat(ingredients.size(), is(4));

        List<Step> steps = LiveDataTestUtil.getValue(
                database.recipeDao().getRecipeSteps(3));
        assertThat(steps, notNullValue());
        assertThat(steps.size(), is(3));
    }

    @Test
    public void insertWholeRecipesThenLoad() throws InterruptedException {
        Recipe recipe1 = TestUtil.createSimpleRecipe(
                1,"Nutella Pie", 8,"");
        recipe1.setIngredients(Arrays.asList(
                createIngredient(1,"CUP", "foo"),
                createIngredient(2,"TBLSP", "bar"),
                createIngredient(3,"K", "Nutella")
        ));
        recipe1.setSteps(Arrays.asList(
                createStep(0,"foo","foo foo", "",""),
                createStep(1,"bar","bar bar", "",""),
                createStep(2,"moo","moo moo", "","")
        ));

        Recipe recipe2 = TestUtil.createSimpleRecipe(
                3, "Yellow Cake", 6,"");
        recipe2.setIngredients(Arrays.asList(
                createIngredient(400,"G", "flour"),
                createIngredient(700,"G", "sugar"),
                createIngredient(2,"TBS", "salt"),
                createIngredient(4,"TSP", "baking powder")
        ));
        recipe2.setSteps(Arrays.asList(
                createStep(0,"loo","loo loo", "",""),
                createStep(1, "coo","coo coo", "",""),
                createStep(2, "moo","moo moo", "","")
        ));

        database.recipeDao().insertAllRecipes(Arrays.asList(recipe1, recipe2));
        List<RecipeDetails> loaded = LiveDataTestUtil.getValue(database.recipeDao().getRecipeDetailsList());
        assertThat(loaded, notNullValue());

        RecipeDetails first = loaded.get(0);
        RecipeDetails second = loaded.get(1);

        assertThat(first.recipe.getName(), is("Nutella Pie"));
        assertThat(first.recipe.getId(), is(1));
        assertThat(first.recipe.getServings(), is(8));
        assertThat(first.ingredients, notNullValue());
        assertThat(first.ingredients.size(), is(3));
        assertThat(first.steps, notNullValue());
        assertThat(first.steps.size(), is(3));

        assertThat(second.recipe.getName(), is("Yellow Cake"));
        assertThat(second.recipe.getId(), is(3));
        assertThat(second.recipe.getServings(), is(6));
        assertThat(second.ingredients, notNullValue());
        assertThat(second.ingredients.size(), is(4));
        assertThat(second.steps, notNullValue());
        assertThat(second.steps.size(), is(3));
    }
}
