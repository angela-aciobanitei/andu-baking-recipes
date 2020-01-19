package com.ang.acb.bakeit.api;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.remote.ApiService;
import com.ang.acb.bakeit.utils.LiveDataCallAdapterFactory;
import com.ang.acb.bakeit.util.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ApiServiceTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private MockWebServer mockWebServer;
    private ApiService service;

    @Before
    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(ApiService.class);
    }

    @After
    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    // Helper methods
    private void enqueueResponse(String fileName, Map<String, String> headers) throws IOException {
        InputStream inputStream = Objects.requireNonNull(getClass().getClassLoader())
                .getResourceAsStream("api-response/" + fileName);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse.setBody(source.readString(StandardCharsets.UTF_8)));
    }

    private void enqueueResponse(String fileName) throws IOException {
        enqueueResponse(fileName, Collections.emptyMap());
    }

    @Test
    public void getAllRecipes() throws IOException, InterruptedException {
        enqueueResponse("baking.json");
        List<Recipe> recipes = LiveDataTestUtil.getValue(service.getAllRecipes()).body;

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/baking.json"));

        assertThat(recipes, notNullValue());
        assertThat(recipes.size(), is(4));

        Recipe recipe1 = recipes.get(0);
        assertThat(recipe1.getName(), is("Nutella Pie"));
        assertThat(recipe1.getServings(), is(8));
        assertThat(recipe1.getImage(), is(""));
        assertThat(recipe1.getIngredients(), notNullValue());
        assertThat(recipe1.getIngredients().size(), is(9));
        assertThat(recipe1.getSteps(), notNullValue());
        assertThat(recipe1.getSteps().size(), is(7));

        Recipe recipe2 = recipes.get(1);
        assertThat(recipe2.getName(), is("Brownies"));
        assertThat(recipe2.getServings(), is(8));
        assertThat(recipe2.getImage(), is(""));
        assertThat(recipe2.getIngredients(), notNullValue());
        assertThat(recipe2.getIngredients().size(), is(10));
        assertThat(recipe2.getSteps(), notNullValue());
        assertThat(recipe2.getSteps().size(), is(10));

        Recipe recipe3 = recipes.get(2);
        assertThat(recipe3.getName(), is("Yellow Cake"));
        assertThat(recipe3.getServings(), is(8));
        assertThat(recipe3.getImage(), is(""));
        assertThat(recipe3.getIngredients(), notNullValue());
        assertThat(recipe3.getIngredients().size(), is(10));
        assertThat(recipe3.getSteps(), notNullValue());
        assertThat(recipe3.getSteps().size(), is(13));

        Recipe recipe4 = recipes.get(3);
        assertThat(recipe4.getName(), is("Cheesecake"));
        assertThat(recipe4.getServings(), is(8));
        assertThat(recipe4.getImage(), is(""));
        assertThat(recipe4.getIngredients(), notNullValue());
        assertThat(recipe4.getIngredients().size(), is(9));
        assertThat(recipe4.getSteps(), notNullValue());
        assertThat(recipe4.getSteps().size(), is(13));
    }

    @Test
    public void getLastRecipe() throws IOException, InterruptedException {
        enqueueResponse("baking.json");
        List<Recipe> recipes = LiveDataTestUtil.getValue(service.getAllRecipes()).body;

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("/baking.json"));

        assertThat(recipes, notNullValue());
        assertThat(recipes.size(), is(4));

        Recipe recipe = recipes.get(3);
        assertThat(recipe.getName(), is("Cheesecake"));
        assertThat(recipe.getServings(), is(8));
        assertThat(recipe.getImage(), is(""));

        assertThat(recipe.getIngredients(), notNullValue());
        assertThat(recipe.getIngredients().size(), is(9));
        assertThat(recipe.getIngredients().get(0).getIngredient(), is("Graham Cracker crumbs"));
        assertThat(recipe.getIngredients().get(1).getIngredient(), is("unsalted butter, melted"));
        assertThat(recipe.getIngredients().get(2).getIngredient(), is("granulated sugar"));
        assertThat(recipe.getIngredients().get(3).getIngredient(), is("salt"));
        assertThat(recipe.getIngredients().get(4).getIngredient(), is("vanilla,divided"));
        assertThat(recipe.getIngredients().get(5).getIngredient(), is("cream cheese, softened"));
        assertThat(recipe.getIngredients().get(6).getIngredient(), is("large whole eggs"));
        assertThat(recipe.getIngredients().get(7).getIngredient(), is("large egg yolks"));
        assertThat(recipe.getIngredients().get(8).getIngredient(), is("heavy cream"));

        assertThat(recipe.getSteps(), notNullValue());
        assertThat(recipe.getSteps().size(), is(13));
        assertThat(recipe.getSteps().get(0).getShortDescription(), is("Recipe Introduction"));
        assertThat(recipe.getSteps().get(1).getShortDescription(), is("Starting prep."));
        assertThat(recipe.getSteps().get(2).getShortDescription(), is("Prep the cookie crust."));
        assertThat(recipe.getSteps().get(3).getShortDescription(), is("Start water bath."));
        assertThat(recipe.getSteps().get(4).getShortDescription(), is("Prebake cookie crust. "));
        assertThat(recipe.getSteps().get(5).getShortDescription(), is("Mix cream cheese and dry ingredients."));
        assertThat(recipe.getSteps().get(6).getShortDescription(), is("Add eggs."));
        assertThat(recipe.getSteps().get(7).getShortDescription(), is("Add heavy cream and vanilla."));
        assertThat(recipe.getSteps().get(8).getShortDescription(), is("Pour batter in pan."));
        assertThat(recipe.getSteps().get(9).getShortDescription(), is("Bake the cheesecake."));
        assertThat(recipe.getSteps().get(10).getShortDescription(), is("Turn off oven and leave cake in."));
        assertThat(recipe.getSteps().get(11).getShortDescription(), is("Remove from oven and cool at room temperature."));
        assertThat(recipe.getSteps().get(12).getShortDescription(), is("Final cooling and set."));
    }

    @Test
    public void getDifferentRecipes() throws IOException, InterruptedException {
        enqueueResponse("baking_different.json");
        List<Recipe> recipes = LiveDataTestUtil.getValue(service.getAllRecipes()).body;

        assertThat(recipes, notNullValue());
        assertThat(recipes.size(), is(3));

        Recipe recipe1 = recipes.get(0);
        assertThat(recipe1.getName(), is("Cheesecake"));
        assertThat(recipe1.getServings(), is(5));
        assertThat(recipe1.getImage(), is(""));

        Recipe recipe2 = recipes.get(1);
        assertThat(recipe2.getName(), is("Yellow Cake"));
        assertThat(recipe2.getServings(), is(6));
        assertThat(recipe2.getImage(), is(""));

        Recipe recipe3 = recipes.get(2);
        assertThat(recipe3.getName(), is("Brownies"));
        assertThat(recipe3.getServings(), is(7));
        assertThat(recipe3.getImage(), is(""));
    }
}
