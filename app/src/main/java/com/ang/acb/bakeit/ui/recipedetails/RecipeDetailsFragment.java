package com.ang.acb.bakeit.ui.recipedetails;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ang.acb.bakeit.data.model.RecipeDetails;

import com.ang.acb.bakeit.databinding.FragmentRecipeDetailsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

public class RecipeDetailsFragment extends Fragment {

    private static final String EXTRA_IS_TWO_PANE = "EXTRA_IS_TWO_PANE";

    private FragmentRecipeDetailsBinding binding;
    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;
    private boolean isTwoPane;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    @Inject
    public NavigationController navigationController;

    // Required empty public constructor
    public RecipeDetailsFragment() {}

    public static RecipeDetailsFragment newInstance(Integer recipeId, boolean isTwoPane) {
        Timber.d("RecipeDetailsFragment created.");
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        args.putBoolean(EXTRA_IS_TWO_PANE, isTwoPane);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
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

        // Report that this fragment would like to populate the options menu.
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getInt(EXTRA_RECIPE_ID);
            isTwoPane = args.getBoolean(EXTRA_IS_TWO_PANE, false);
        }

        initViewModel();
        setupAdapters();
        populateUi();
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(RecipeDetailsViewModel.class);
        viewModel.init(recipeId);
    }

    private void setupAdapters() {
        binding.rvIngredients.setAdapter(new IngredientAdapter());
        binding.rvSteps.setAdapter(new StepAdapter(position ->
                // On item click, navigate to StepDetailsFragment.
                navigationController.navigateToStepDetails(recipeId, position, isTwoPane)));
    }

    private void populateUi() {
        viewModel.getRecipeDetailsLiveData().observe(
                getViewLifecycleOwner(), recipeDetails -> {
                    // Bind recipe data.
                    binding.setRecipeDetails(recipeDetails);
                    // Necessary because Espresso cannot read data binding callbacks.
                    binding.executePendingBindings();
                    // Set recipe title for the action bar
                    Objects.requireNonNull(getActivity())
                            .setTitle(recipeDetails.getRecipe().getName());
                }
        );
    }
}
