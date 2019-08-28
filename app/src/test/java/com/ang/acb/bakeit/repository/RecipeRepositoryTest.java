package com.ang.acb.bakeit.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.ang.acb.bakeit.data.local.RecipeLocalDataSource;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.data.remote.RecipeRemoteDataSource;
import com.ang.acb.bakeit.data.repository.RecipeRepository;
import com.ang.acb.bakeit.util.InstantAppExecutors;
import com.ang.acb.bakeit.util.TestUtil;
import com.ang.acb.bakeit.utils.AppExecutors;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
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
        when(localDataSource.loadAllRecipes()).thenReturn(dbData);

        LiveData<Resource<List<Recipe>>> data = repository.loadAllRecipes();
        verify(localDataSource).loadAllRecipes();
        verify(remoteDataSource, never()).loadAllRecipes();
        // TODO Finish this
    }

    @Test
    public void getWholeRecipe() {
        Recipe simpleRecipe = TestUtil.createSimpleRecipe("Nutella Pie", 8);
        
    }


}
