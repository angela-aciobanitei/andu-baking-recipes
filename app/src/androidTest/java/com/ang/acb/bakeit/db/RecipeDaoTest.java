package com.ang.acb.bakeit.db;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.util.LiveDataTestUtil;
import com.ang.acb.bakeit.util.TestUtil;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

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
        Recipe simpleRecipe = TestUtil.createSimpleRecipe("Nutella Pie", 8);
        db.recipeDao().insertRecipe(simpleRecipe);
        Recipe loaded = LiveDataTestUtil.getValue(db.recipeDao().loadSimpleRecipe(Recipe.UNKNOWN_ID));
        assertThat(loaded, notNullValue());
        assertThat(loaded.getName(), is("Nutella Pie"));
        assertThat(loaded.getServings(), is(8));
        assertThat(loaded.getSteps(), nullValue());
        assertThat(loaded.getIngredients(), nullValue());
    }

    @Test
    public void insertWholeRecipe() throws InterruptedException {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe(0,"Nutella Pie", 8);
        simpleRecipe.setIngredients(Arrays.asList(
                createIngredient(0, 0,1,
                                "CUP", "Graham Cracker crumbs"),
                createIngredient(1, 0, 6,
                                "TBLSP", "unsalted butter, melted"),
                createIngredient(2, 0, 0.5,
                                "CUP", "granulated sugar"),
                createIngredient(3, 0, 1.5,
                                "TSP", "salt"),
                createIngredient(4, 0,5,
                                "TBLSP", "vanilla"),
                createIngredient(5, 0,500,
                                "G", "Mascapone Cheese"),
                createIngredient(6, 0,1,
                                "K", "Nutella")
        ));
        simpleRecipe.setSteps(Arrays.asList(
                createStep(
                        0,
                        0,
                        "Recipe Introduction",
                        "Recipe Introduction",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                        ""),
                createStep(
                        1,
                        0,
                        "Starting prep",
                        "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                        "",
                        ""),
                createStep(
                        2,
                        0,
                        "Prep the cookie crust.",
                        "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4",
                        ""),
                createStep(
                        3,
                        0,
                        "Press the crust into baking form.",
                        "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4",
                        ""),
                createStep(
                        4,
                        0,
                        "Start filling prep",
                        "4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4",
                        ""),
                createStep(
                        5,
                        0,
                        "Finish filling prep",
                        "5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.",
                        "",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"),
                createStep(
                        6,
                        0,
                        "Finishing Steps",
                        "6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4",
                        "")
        ));

        db.recipeDao().insertAllRecipes(Collections.singletonList(simpleRecipe));
        WholeRecipe loaded = LiveDataTestUtil.getValue(db.recipeDao().loadWholeRecipe(0));
        assertThat(loaded, notNullValue());
        assertThat(loaded.getRecipe().getName(), is("Nutella Pie"));
        assertThat(loaded.getRecipe().getServings(), is(8));
        assertThat(loaded.getIngredients(), notNullValue());
        assertThat(loaded.getIngredients().size(), is(7));
        assertThat(loaded.getSteps(), notNullValue());
        assertThat(loaded.getSteps().size(), is(7));
    }
}
