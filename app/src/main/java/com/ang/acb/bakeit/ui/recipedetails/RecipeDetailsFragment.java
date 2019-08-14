package com.ang.acb.bakeit.ui.recipedetails;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.bakeit.data.model.WholeRecipe;

import com.ang.acb.bakeit.databinding.FragmentRecipeDetailsBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("RecipeDetailsFragment binding created.");
        binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();
        setupIngredientsAdapter();
        setupStepsAdapter();
        observeResult();
    }

    private void setupViewModel() {
        // Create view model
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(getActivity(),factory)
                .get(RecipeDetailsViewModel.class);
        Timber.d("Recipe [id=%s]: create view model.", recipeId);

        // Get bundle args (with thw recipe id) and init recipe details view model.
        Bundle args = getArguments();
        if (args != null) recipeId = args.getInt(EXTRA_RECIPE_ID);

        viewModel.init(recipeId);
        Timber.d("Recipe [id=%s]: init view model.", recipeId);
    }

    private void setupIngredientsAdapter() {
        RecyclerView rvIngredients = binding.rvIngredients;
        rvIngredients.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.HORIZONTAL, false));
        rvIngredients.setHasFixedSize(true);
        rvIngredients.setAdapter(new IngredientAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvIngredients, false);
        Timber.d("Recipe [id=%s]: setup ingredients adapter.", recipeId);
    }

    private void setupStepsAdapter() {
        RecyclerView rvSteps = binding.rvSteps;
        rvSteps.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.HORIZONTAL, false));
        rvSteps.setHasFixedSize(true);
        rvSteps.setAdapter(new StepAdapter());
        // Disable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvSteps, false);
        Timber.d("Recipe [id=%s]: setup steps adapter.", recipeId);
    }

    private void observeResult() {
        // FIXME: Observe recipe details
        viewModel.getWholeRecipeLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<WholeRecipe>() {
                    @Override
                    public void onChanged(WholeRecipe wholeRecipe) {
                        binding.setWholeRecipe(wholeRecipe);
                    }
                }
        );

    }
}
