package com.ang.acb.bakeit.db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.util.LiveDataTestUtil;
import com.ang.acb.bakeit.util.TestUtil;

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
public class RecipeDaoTest extends DbTest {

    @Rule
    // See: https://stackoverflow.com/questions/52274924/cannot-invoke-observeforever-on-a-background-thread
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void insertSimpleRecipe() throws InterruptedException {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe(
                1, "Nutella Pie", 8, "");
        db.recipeDao().insertRecipe(simpleRecipe);
        Recipe loaded = LiveDataTestUtil.getValue(
                db.recipeDao().loadSimpleRecipe(Recipe.UNKNOWN_ID));
        assertThat(loaded, notNullValue());
        assertThat(loaded.getName(), is("Nutella Pie"));
        assertThat(loaded.getServings(), is(8));
        assertThat(loaded.getSteps(), nullValue());
        assertThat(loaded.getIngredients(), nullValue());
    }

    @Test
    public void insertWholeRecipe() throws InterruptedException {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe(
                1, "Nutella Pie", 8, "");

        simpleRecipe.setIngredients(Arrays.asList(
                createIngredient(1,"CUP", "foo"),
                createIngredient(2,"TBLSP", "bar"),
                createIngredient(3,"K", "Nutella")
        ));
        simpleRecipe.setSteps(Arrays.asList(
                createStep(0, "foo","foo foo", "",""),
                createStep(1, "bar","bar bar", "",""),
                createStep(2, "moo","moo moo", "","")
        ));

        db.recipeDao().insertAllRecipes(Collections.singletonList(simpleRecipe));
        WholeRecipe loaded = LiveDataTestUtil.getValue(db.recipeDao().loadWholeRecipe(1));
        assertThat(loaded, notNullValue());
        assertThat(loaded.getRecipe().getName(), is("Nutella Pie"));
        assertThat(loaded.getRecipe().getServings(), is(8));

        List<Ingredient> ingredients = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeIngredients(loaded.getRecipe().getId()));
        assertThat(ingredients, notNullValue());
        assertThat(ingredients.size(), is(3));

        List<Step> steps = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeSteps(loaded.getRecipe().getId()));
        assertThat(steps, notNullValue());
        assertThat(steps.size(), is(3));
    }

    @Test
    public void insertWholeRecipes() throws InterruptedException {
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

        db.recipeDao().insertAllRecipes(Arrays.asList(recipe1, recipe2));
        List<WholeRecipe> loaded = LiveDataTestUtil.getValue(db.recipeDao().loadWholeRecipes());
        assertThat(loaded, notNullValue());

        assertThat(loaded.get(0).getRecipe().getName(), is("Nutella Pie"));
        assertThat(loaded.get(0).getRecipe().getId(), is(1));
        assertThat(loaded.get(0).getRecipe().getServings(), is(8));

        List<Ingredient> recipe1Ingredients = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeIngredients(loaded.get(0).getRecipe().getId()));
        assertThat(recipe1Ingredients, notNullValue());
        assertThat(recipe1Ingredients.size(), is(3));

        List<Step> recipe1steps = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeSteps(loaded.get(0).getRecipe().getId()));
        assertThat(recipe1steps, notNullValue());
        assertThat(recipe1steps.size(), is(3));

        assertThat(loaded.get(1).getRecipe().getName(), is("Yellow Cake"));
        assertThat(loaded.get(1).getRecipe().getId(), is(3));
        assertThat(loaded.get(1).getRecipe().getServings(), is(6));

        List<Ingredient> recipe2Ingredients = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeIngredients(loaded.get(1).getRecipe().getId()));
        assertThat(recipe2Ingredients, notNullValue());
        assertThat(recipe2Ingredients.size(), is(4));

        List<Step> recipe2steps = LiveDataTestUtil.getValue(
                db.recipeDao().loadRecipeSteps(loaded.get(1).getRecipe().getId()));
        assertThat(recipe2steps, notNullValue());
        assertThat(recipe2steps.size(), is(3));

    }
}
