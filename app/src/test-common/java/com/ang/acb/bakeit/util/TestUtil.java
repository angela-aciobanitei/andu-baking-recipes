package com.ang.acb.bakeit.util;

import com.ang.acb.bakeit.data.model.Ingredient;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Step;

import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static Recipe createSimpleRecipe(Integer recipeId, String name, Integer servings, String image) {
       Recipe recipe =  new Recipe();
       recipe.setId(recipeId);
       recipe.setName(name);
       recipe.setServings(servings);
       recipe.setImage(image);
       return recipe;
    }

    public static Ingredient createIngredient(double quantity, String measure, String ingredient) {
        return new Ingredient(quantity, measure, ingredient);
    }

    public static Step createStep(Integer index, String shortDescription, String description, String videoURL, String thumbnailURL) {
        return new Step(index, shortDescription, description,videoURL, thumbnailURL);
    }

    public static List<Recipe> createRecipeList() {
        Recipe carrotCake = TestUtil.createSimpleRecipe(
                5, "Carrot Cake", 4, "");
        carrotCake.setIngredients(Arrays.asList(
                createIngredient(1,"CUP", "foo"),
                createIngredient(5,"TBLSP", "bar")
        ));
        carrotCake.setSteps(Arrays.asList(
                createStep(0, "foo","foo foo", "",""),
                createStep(1, "bar","bar bar", "","")
        ));

        Recipe blueberryPie = TestUtil.createSimpleRecipe(
                6, "Blueberry Pie", 6, "");
        blueberryPie.setIngredients(Arrays.asList(
                createIngredient(2,"CUP", "a"),
                createIngredient(7,"TBLSP", "b"),
                createIngredient(500,"G", "c")
        ));
        blueberryPie.setSteps(Arrays.asList(
                createStep(0, "prepare","foo foo", "",""),
                createStep(1, "bake","bar bar", "",""),
                createStep(2, "serve","bon appetit", "","")
        ));

        return Arrays.asList(carrotCake, blueberryPie);
    }

    public static RecipeDetails createDetailedRecipe() {
        RecipeDetails recipeDetails = new RecipeDetails();
        recipeDetails.setRecipe(TestUtil.createSimpleRecipe(
                0,"Nutella Pie", 8, ""));
        recipeDetails.setIngredients(Arrays.asList(
                createIngredient( 1,"CUP", "Graham Cracker crumbs"),
                createIngredient(6,"TBLSP", "unsalted butter, melted"),
                createIngredient(0.5,"CUP", "granulated sugar"),
                createIngredient(1.5, "TSP", "salt"),
                createIngredient(5,"TBLSP", "vanilla"),
                createIngredient(500,"G", "Mascapone Cheese"),
                createIngredient(1,"K", "Nutella")
        ));
        recipeDetails.setSteps(Arrays.asList(
                createStep(
                        0,
                        "Recipe Introduction",
                        "Recipe Introduction",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                        ""),
                createStep(
                        1,
                        "Starting prep",
                        "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                        "",
                        ""),
                createStep(
                        2,
                        "Prep the cookie crust.",
                        "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4",
                        ""),
                createStep(
                        3,
                        "Press the crust into baking form.",
                        "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4",
                        ""),
                createStep(
                        4,
                        "Start filling prep",
                        "4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4",
                        ""),
                createStep(
                        5,
                        "Finish filling prep",
                        "5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.",
                        "",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"),
                createStep(
                        6,
                        "Finishing Steps",
                        "6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!",
                        "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4",
                        "")
        ));

        return  recipeDetails;

    }

}
