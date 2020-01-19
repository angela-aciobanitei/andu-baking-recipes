package com.ang.acb.bakeit.ui.recipelist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.bakeit.databinding.FragmentRecipeListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

/**
 * A {@link Fragment} that displays a list of recipes.
 */
public class RecipeListFragment extends Fragment {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";
    public static final String EXTRA_RECIPE_NAME ="EXTRA_RECIPE_NAME";
    public static final Integer INVALID_RECIPE_ID = -1;

    private FragmentRecipeListBinding binding;
    private RecipeListViewModel viewModel;
    private RecipeAdapter adapter;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public RecipeListFragment() {}

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger with Fragments, inject as early as possible.
        // This prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initViewModel();
        initRecipeAdapter();
        populateUi();
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
