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

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.RecipeDetails;
import com.ang.acb.bakeit.databinding.FragmentRecipeDetailsBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;

import org.jetbrains.annotations.NotNull;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeDetailsFragment extends Fragment {

    private FragmentRecipeDetailsBinding binding;
    private RecipeDetailsViewModel viewModel;

    public RecipeDetailsFragment() {}

    public static RecipeDetailsFragment newInstance(Long recipeId) {
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_RECIPE_ID, recipeId);
        fragment.setArguments(args);

        return fragment;
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

        // Get bundle args (with thw recipe id) and init recipe details view model.
        Bundle args = getArguments();
        if (args != null) viewModel.init(args.getLong(EXTRA_RECIPE_ID));
    }

    private void setupIngredientsAdapter() {
        RecyclerView rvIngredients = binding.rvIngredients;
        rvIngredients.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.HORIZONTAL, false));
        rvIngredients.setHasFixedSize(true);
        rvIngredients.setAdapter(new IngredientAdapter());
        // Note: remember to enable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvIngredients, false);
    }

    private void setupStepsAdapter() {
        RecyclerView rvSteps = binding.rvSteps;
        rvSteps.setLayoutManager(new LinearLayoutManager(
                getContext(), RecyclerView.HORIZONTAL, false));
        rvSteps.setHasFixedSize(true);
        rvSteps.setAdapter(new StepAdapter());
        // Note: remember to enable nested scrolling for this view.
        ViewCompat.setNestedScrollingEnabled(rvSteps, false);
    }

    private void observeResult() {
        // FIXME Observe result.
        viewModel.getRecipeDetailsLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<RecipeDetails>() {
                    @Override
                    public void onChanged(RecipeDetails recipeDetails) {
                        binding.setRecipeDetails(recipeDetails);
                        // FIXME: binding.setMovieDetails(resource.data);
                        // FIXME: binding.setResource(resource);
                    }
        });

        // FIXME Handle retry event in case of network failure.
        // binding.networkState.retryButton.setOnClickListener(view -> viewModel.retry(movieId));
    }
}
