package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Recipe;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.ActivityRecipeListBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import java.util.List;

import timber.log.Timber;

public class RecipeListActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final Integer INVALID_RECIPE_ID = -1;

    private ActivityRecipeListBinding binding;
    private RecipeListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(this);
        viewModel = ViewModelProviders.of(this, factory).get(RecipeListViewModel.class);
        Timber.d("Setup recipe list view model.");

        // Setup recycler view.
        final RecipeAdapter adapter =  new RecipeAdapter(viewModel);
        RecyclerView recyclerView = findViewById(R.id.rv_recipe_list);
        recyclerView.setAdapter(adapter);
        Timber.d("Setup recipe list recycler view.");

        // Observe data and network status.
        viewModel.getRecipeListResourceLiveData().observe(this,
            new Observer<Resource<List<Recipe>>>() {
                @Override
                public void onChanged(Resource<List<Recipe>> resource) {
                    Timber.d("Observe recipe list from recipe list view model.");
                    adapter.submitList(resource);
                    Timber.d("Observe network status from recipe list view model.");
                    adapter.setNetworkState(resource);
                }
            });
    }

}
