package com.ang.acb.bakeit.ui.recipedetails;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.FragmentStepDetailsBinding;

import com.ang.acb.bakeit.utils.GlideApp;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

import static com.ang.acb.bakeit.ui.recipedetails.DetailsActivity.EXTRA_IS_TWO_PANE;
import static com.ang.acb.bakeit.ui.recipedetails.DetailsActivity.EXTRA_STEP_POSITION;
import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

public class StepDetailsFragment extends Fragment  {

    private static final String CURRENT_STEP_INDEX_KEY = "CURRENT_STEP_INDEX_KEY";
    private static final String CURRENT_PLAYBACK_POSITION_KEY = "CURRENT_PLAYBACK_POSITION_KEY";
    private static final String CURRENT_WINDOW_KEY ="CURRENT_WINDOW_KEY";
    private static final String SHOULD_PLAY_WHEN_READY_KEY = "SHOULD_PLAY_WHEN_READY_KEY";

    private FragmentStepDetailsBinding binding;
    private StepDetailsViewModel viewModel;
    private Integer recipeId;
    private int currentStepIndex;
    private boolean isTwoPane;

    private SimpleExoPlayer simpleExoPlayer;
    private boolean shouldPlayWhenReady;
    private long currentPlaybackPosition;
    private int currentWindowIndex;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    // Required empty public constructor
    public StepDetailsFragment() {}

    public static StepDetailsFragment newInstance(Integer recipeId, int stepPosition, boolean isTwoPane) {
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        args.putInt(EXTRA_STEP_POSITION, stepPosition);
        args.putBoolean(EXTRA_IS_TWO_PANE, isTwoPane);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        // When using Dagger with Fragments, inject as early as possible.
        // This prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStepDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isFullscreenMode()) hideSystemUi();
        getArgExtras();
        initViewModel();
        restoreInstanceState(savedInstanceState);
        observeCurrentStep();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP_INDEX_KEY, currentStepIndex);
        outState.putLong(CURRENT_PLAYBACK_POSITION_KEY, currentPlaybackPosition);
        outState.putInt(CURRENT_WINDOW_KEY, currentWindowIndex);
        outState.putBoolean(SHOULD_PLAY_WHEN_READY_KEY, shouldPlayWhenReady);
    }

    private void restoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_STEP_INDEX_KEY)) {
                currentStepIndex = savedInstanceState.getInt(CURRENT_STEP_INDEX_KEY);
            }
            if (savedInstanceState.containsKey(CURRENT_WINDOW_KEY)) {
                currentWindowIndex = savedInstanceState.getInt(CURRENT_WINDOW_KEY);
            }
            if (savedInstanceState.containsKey(CURRENT_PLAYBACK_POSITION_KEY)) {
                currentPlaybackPosition = savedInstanceState.getLong(CURRENT_PLAYBACK_POSITION_KEY);
            }
            if (savedInstanceState.containsKey(SHOULD_PLAY_WHEN_READY_KEY)) {
                shouldPlayWhenReady = savedInstanceState.getBoolean(SHOULD_PLAY_WHEN_READY_KEY);
            }
        }
    }

    private void getArgExtras() {
        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getInt(EXTRA_RECIPE_ID);
            currentStepIndex = args.getInt(EXTRA_STEP_POSITION);
            isTwoPane = args.getBoolean(EXTRA_IS_TWO_PANE);
        }
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(StepDetailsViewModel.class);
        viewModel.init(recipeId, currentStepIndex);
    }

    private void observeCurrentStep(){
        viewModel.getCurrentStep().observe(getViewLifecycleOwner(), stepResource -> {
            // Make Step data available to data binding.
            binding.setStep(stepResource.data);
            binding.setStepCount(viewModel.getStepsSize());
            if (stepResource.data != null) {
                handleStepUrl(stepResource.data);
            }
            handleStepButtons();
        });
    }

    private void handleStepUrl(Step step){
        // If step has a video, initialize player, else display an image.
        String videoUrl = step.getVideoURL();
        if (!TextUtils.isEmpty(videoUrl)) {
            initializePlayer(Uri.parse(videoUrl));
        } else {
            String thumbnailUrl = step.getThumbnailURL();
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                GlideApp.with(this)
                        .load(thumbnailUrl)
                        // Display a placeholder while the image is loading.
                        .placeholder(R.drawable.loading_animation)
                        // Provide an error placeholder for non-existing URLs.
                        .error(R.color.colorImagePlaceholder)
                        // Provide a fallback image resource for null URLs.
                        .fallback(R.drawable.baking)
                        .into(binding.placeholderImage);
            } else {
                // Provide a fallback for an empty thumbnail URL.
                binding.placeholderImage.setImageResource(R.drawable.baking);
            }
        }
    }

    private void handleStepButtons(){
        if (!isTwoPane && !isFullscreenMode()) {
            binding.previousStepButton.setOnClickListener(view -> {
                resetPlayer();
                viewModel.onPrevious();
                currentStepIndex--;
            });

            binding.nextStepButton.setOnClickListener(view -> {
                resetPlayer();
                viewModel.onNext();
                currentStepIndex++;
            });

            // Necessary because Espresso cannot read data binding callbacks.
            binding.executePendingBindings();
        }
    }


    private void initializePlayer(Uri mediaUri) {
        // See: https://exoplayer.dev/hello-world
        if (simpleExoPlayer == null) {
            // Create the player using the ExoPlayerFactory.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    Objects.requireNonNull(getContext()));

            // Attach the payer to the view.
            binding.exoplayerView.setPlayer(simpleExoPlayer);
        }

        // Create a media source representing the media to be played.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(), getString(R.string.app_name)));
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);

        // Prepare the player.
        simpleExoPlayer.prepare(mediaSource);

        // Control the player.
        simpleExoPlayer.seekTo(currentPlaybackPosition);
        simpleExoPlayer.setPlayWhenReady(shouldPlayWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            currentPlaybackPosition = simpleExoPlayer.getCurrentPosition();
            currentWindowIndex = simpleExoPlayer.getCurrentWindowIndex();
            shouldPlayWhenReady = simpleExoPlayer.getPlayWhenReady();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void resetPlayer() {
        shouldPlayWhenReady = true;
        currentPlaybackPosition = 0;
        currentWindowIndex = 0;
        if (simpleExoPlayer != null) simpleExoPlayer.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Release the player in onPause() if on Android Marshmallow and below.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Release the player in onStop() if on Android Nougat and above
        // because of the multi window support that was added in Android N.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            releasePlayer();
        }
    }

    private boolean isFullscreenMode() {
        Configuration configuration = getResources().getConfiguration();
        return configuration.smallestScreenWidthDp < 600 &&
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void hideSystemUi() {
        // See: https://developer.android.com/training/system-ui/immersive#EnableFullscreen
        Objects.requireNonNull(getActivity()).getWindow().getDecorView().setSystemUiVisibility(
                // Enables regular immersive mode.
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                // Hide the nav bar and status bar.
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Objects.requireNonNull(getActivity()).onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
