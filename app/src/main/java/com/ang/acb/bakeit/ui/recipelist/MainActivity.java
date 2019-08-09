package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.ActivityMainBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

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

        // TODO Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(this);
        RecipeListViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(RecipeListViewModel.class);
        Timber.d("Setup recipe list view model.");

        // TODO Setup recycler view
        final RecipeAdapter adapter =  new RecipeAdapter();
        RecyclerView recyclerView = this.findViewById(R.id.rv_recipe_list);
        recyclerView.setAdapter(adapter);
        Timber.d("Setup recipe list recycler view.");

        // TODO Observe data
        viewModel.getRecipes().observe(this, listResource -> {
            if (listResource != null && listResource.status == Resource.Status.SUCCESS) {
                adapter.setRecipeList(listResource.getData());
            } else if (listResource != null && listResource.status == Resource.Status.ERROR) {
                Toast.makeText(this,R.string.no_internet_connection_toast_message, Toast.LENGTH_LONG).show();
            }
        });
        Timber.d("Observe recipe list from view model.");



    }

}
