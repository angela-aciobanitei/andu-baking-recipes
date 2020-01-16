package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.databinding.ActivityMainBinding;

import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME ="EXTRA_RECIPE_NAME";
    public static final Integer INVALID_RECIPE_ID = -1;

    private ActivityMainBinding binding;
    private RecipeListViewModel viewModel;
    private RecipeAdapter adapter;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // When using Dagger with Activities, inject as early as possible.
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        initBinding();
        initViewModel();
        initRecipeAdapter();
        populateUi();
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
    }

    private void initRecipeAdapter() {
        adapter =  new RecipeAdapter();
        binding.rvRecipeList.setAdapter(adapter);
    }

    private void populateUi() {
        // Observe data and network status.
        viewModel.getLiveRecipes().observe(this, resource -> {
            if (resource != null && resource.data != null) {
                adapter.submitList(resource.data
                        .stream()
                        .map(item -> item.recipe)
                        .collect(Collectors.toList()));
            }

            binding.setResource(resource);
            binding.setCallback(() -> viewModel.retry());
            binding.executePendingBindings();
        });
    }
}
