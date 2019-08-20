package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.ActivityRecipeListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME ="EXTRA_RECIPE_NAME";
    public static final Integer INVALID_RECIPE_ID = -1;

    private RecipeListViewModel viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupBinding();
        setupViewModel();
        observeResult(getRecipeAdapter());
    }

    private void setupBinding() {
        // Inflate view and obtain an instance of the binding class.
        ActivityRecipeListBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_recipe_list);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeListViewModel.class);
        Timber.d("Setup recipe list view model.");
    }

    @NotNull
    private RecipeAdapter getRecipeAdapter() {
        final RecipeAdapter adapter =  new RecipeAdapter(viewModel);
        RecyclerView recyclerView = findViewById(R.id.rv_recipe_list);
        recyclerView.setAdapter(adapter);
        Timber.d("Setup recipe list adapter.");
        return adapter;
    }

    private void observeResult(RecipeAdapter adapter) {
        // Observe data and network status.
        viewModel.getRecipeListResourceLiveData().observe(this,
            new Observer<Resource<List<Recipe>>>() {
                @Override
                public void onChanged(Resource<List<Recipe>> resource) {
                    Timber.d("Observe recipe list.");
                    adapter.submitList(resource);
                    Timber.d("Observe network status.");
                    adapter.setNetworkState(resource);
                }
        });
    }

}
