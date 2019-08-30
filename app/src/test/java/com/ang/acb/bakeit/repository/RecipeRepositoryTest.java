package com.ang.acb.bakeit.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ang.acb.bakeit.data.local.RecipeLocalDataSource;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.remote.ApiResponse;
import com.ang.acb.bakeit.data.remote.RecipeRemoteDataSource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.util.InstantAppExecutors;
import com.ang.acb.bakeit.util.TestUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ang.acb.bakeit.util.ApiUtil.successCall;
import static com.ang.acb.bakeit.util.TestUtil.createIngredient;
import static com.ang.acb.bakeit.util.TestUtil.createStep;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class RecipeRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private RecipeLocalDataSource localDataSource;
    private RecipeRemoteDataSource remoteDataSource;
    private RecipeRepository repository;

    @Before
    public void setup() {
        localDataSource = Mockito.mock(RecipeLocalDataSource.class);
        remoteDataSource = Mockito.mock(RecipeRemoteDataSource.class);
        repository = new RecipeRepository(
                localDataSource,
                remoteDataSource,
                new InstantAppExecutors());
    }

    @Test
    public void loadAllRecipes() throws IOException {
        MutableLiveData<List<Recipe>> dbData = new MutableLiveData<>();
        when(localDataSource.getRecipeDetailsList()).thenReturn(dbData);

        LiveData<Resource<List<Recipe>>> loadedData = repository.loadAllRecipes();
        verify(localDataSource).getRecipeDetailsList();
        verify(remoteDataSource, never()).loadAllRecipes();

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

        List<Recipe> recipes = Arrays.asList(carrotCake, blueberryPie);
        LiveData<ApiResponse<List<Recipe>>> call = successCall(recipes);
        when(remoteDataSource.loadAllRecipes()).thenReturn(call);

        Observer<Resource<List<Recipe>>> observer = mock(Observer.class);
        loadedData.observeForever(observer);
        verify(observer).onChanged(Resource.loading( null));

        MutableLiveData<List<Recipe>> updatedDbData = new MutableLiveData<>();
        when(localDataSource.getRecipeDetailsList()).thenReturn(updatedDbData);
        dbData.setValue(Collections.emptyList());

        verify(remoteDataSource).loadAllRecipes();
        ArgumentCaptor<List<Recipe>> inserted = ArgumentCaptor.forClass((Class) List.class);
        verify(localDataSource).saveAllRecipes(inserted.capture());

        assertThat(inserted.getValue().size(), is(2));
        Recipe first = inserted.getValue().get(0);
        assertThat(first.getName(), is("Carrot Cake"));
        assertThat(first.getServings(), is(4));
        assertThat(first.getId(), is(5));
        assertThat(first.getIngredients(), notNullValue());
        assertThat(first.getIngredients().size(), is(2));
        assertThat(first.getSteps(), notNullValue());
        assertThat(first.getSteps().size(), is(2));

        Recipe second = inserted.getValue().get(1);
        assertThat(second.getName(), is("Blueberry Pie"));
        assertThat(second.getServings(), is(6));
        assertThat(second.getId(), is(6));
        assertThat(second.getIngredients(), notNullValue());
        assertThat(second.getIngredients().size(), is(3));
        assertThat(second.getSteps(), notNullValue());
        assertThat(second.getSteps().size(), is(3));

        updatedDbData.setValue(recipes);
        verify(observer).onChanged(Resource.success(recipes));
    }


}
