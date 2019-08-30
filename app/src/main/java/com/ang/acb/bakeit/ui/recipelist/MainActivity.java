package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.IdlingResource;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.ActivityMainBinding;
import com.ang.acb.bakeit.utils.RecipeIdlingResource;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME ="EXTRA_RECIPE_NAME";
    public static final Integer INVALID_RECIPE_ID = -1;

    ActivityMainBinding binding;
    private RecipeIdlingResource idlingResource;
    private RecipeListViewModel viewModel;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Note: when using Dagger for injecting Activity
        // objects, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        initBinding();
        initViewModel();
        observeResult(getRecipeAdapter());
        getIdlingResource();
    }

    private void initBinding() {
        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeListViewModel.class);
        Timber.d("Setup recipe list view model.");
    }

    @NotNull
    private RecipeAdapter getRecipeAdapter() {
        final RecipeAdapter adapter =  new RecipeAdapter(viewModel);
        RecyclerView recyclerView = binding.rvRecipeList;
        recyclerView.setAdapter(adapter);
        Timber.d("Setup recipe list adapter.");
        return adapter;
    }

    private void observeResult(RecipeAdapter adapter) {
        // Observe data and network status.
        viewModel.getRecipesLiveData().observe(this,
            new Observer<Resource<List<Recipe>>>() {
                @Override
                public void onChanged(Resource<List<Recipe>> resource) {
                    Timber.d("Observe recipe list.");
                    if (resource != null && resource.data != null) {
                        adapter.submitList(resource.data);
                    } else {
                        adapter.submitList(Collections.emptyList());
                    }
                    Timber.d("Observe network status.");
                    binding.setResource(resource);
                    // FIXME: HANDLE retries
                    Timber.d("Handle retries.");
                    binding.setCallback(() -> viewModel.retry());
                    binding.executePendingBindings();
                }
        });
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipeIdlingResource();
        }
        return idlingResource;
    }

}
