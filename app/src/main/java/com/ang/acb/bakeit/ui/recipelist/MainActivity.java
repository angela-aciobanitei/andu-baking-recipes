package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.ActivityMainBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import java.util.List;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final Long INVALID_RECIPE_ID = (long) -1;

    private ActivityMainBinding binding;
    private RecipeListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate view and obtain an instance of the binding class.
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Specify the current activity as the lifecycle owner.
        binding.setLifecycleOwner(this);

        // Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(this);
        RecipeListViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(RecipeListViewModel.class);
        Timber.d("Setup recipe list view model.");

        // Setup recycler view.
        final RecipeAdapter adapter =  new RecipeAdapter(viewModel);
        RecyclerView recyclerView = this.findViewById(R.id.rv_recipe_list);
        recyclerView.setAdapter(adapter);
        Timber.d("Setup recipe list recycler view.");

        // FIXME Observe data and network status.
        viewModel.getRecipeListResourceLiveData().observe(this,
                new Observer<Resource<List<RecipeDetails>>>() {
                    @Override
                    public void onChanged(Resource<List<RecipeDetails>> resource) {
                        Timber.d("Observe recipe list from view model.");
                        adapter.submitList(resource);
                        Timber.d("Observe network status from view model.");
                        adapter.setNetworkState(resource);
                    }
                });

    }

}
