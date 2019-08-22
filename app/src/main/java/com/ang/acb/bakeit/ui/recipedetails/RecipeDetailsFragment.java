package com.ang.acb.bakeit.ui.recipedetails;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.bakeit.data.model.WholeRecipe;

import com.ang.acb.bakeit.databinding.FragmentRecipeDetailsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // Required empty public constructor
    public RecipeDetailsFragment() {}

    public static RecipeDetailsFragment newInstance(Integer recipeId) {
        Timber.d("RecipeDetailsFragment created.");
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        // Note: when using Dagger for injecting Fragment objects, inject as early as possible.
        // For this reason, call AndroidInjection.inject() in onAttach(). This also prevents
        // inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeViewModel();
        setupIngredientsAdapter();
        setupStepsAdapter();
        observeRecipeDetails();
    }

    private void initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeDetailsViewModel.class);

        if (getArguments() != null) recipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        viewModel.init(recipeId);
    }

    private void setupIngredientsAdapter() {
        RecyclerView rvIngredients = binding.rvIngredients;
        rvIngredients.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        rvIngredients.setHasFixedSize(true);
        rvIngredients.setAdapter(new IngredientAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvIngredients, false);
        Timber.d("Recipe [id=%s]: setup ingredients adapter.", recipeId);
    }

    private void setupStepsAdapter() {
        RecyclerView rvSteps = binding.rvSteps;
        rvSteps.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false));
        rvSteps.setHasFixedSize(true);
        rvSteps.setAdapter(new StepAdapter(viewModel));
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvSteps, false);
        Timber.d("Recipe [id=%s]: setup steps adapter.", recipeId);
    }

    private void observeRecipeDetails() {
        Timber.d("Recipe [id=%s]: observe recipe details.", recipeId);
        viewModel.getWholeRecipeLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<WholeRecipe>() {
                    @Override
                    public void onChanged(WholeRecipe wholeRecipe) {
                        // Bind recipe data
                        binding.setWholeRecipe(wholeRecipe);
                        // Set recipe title for the action bar
                        Objects.requireNonNull(getActivity())
                                .setTitle(wholeRecipe.getRecipe().getName());
                    }
                }
        );
    }

}
