package com.ang.acb.bakeit.ui.recipelist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Resource;
import com.ang.acb.bakeit.databinding.FragmentRecipeListBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import timber.log.Timber;

/**
 * The UI Controller for displaying a list of recipes.
 */
public class RecipeListFragment extends Fragment {

    FragmentRecipeListBinding binding;

    public static RecipeListFragment newInstance(){
        return new RecipeListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO Setup view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getActivity());
        RecipeListViewModel viewModel = ViewModelProviders
                .of(getActivity(), factory)
                .get(RecipeListViewModel.class);
        Timber.d("Setup RecipesViewModel");

        // TODO Setup recycler view
        final RecipeAdapter adapter =  new RecipeAdapter();
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = getActivity().findViewById(R.id.rv_recipe_list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        Timber.d("Setup recipe list recycler view");

        // TODO Observe data
        viewModel.getRecipes().observe(getViewLifecycleOwner(), recipesViews -> {
            if (recipesViews != null && recipesViews.status == Resource.Status.SUCCESS) {
                adapter.setRecipeList(recipesViews.data);
            } else if (recipesViews != null && recipesViews.status == Resource.Status.ERROR) {
                Toast.makeText(getActivity(),
                        R.string.no_internet_connection_toast_message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
