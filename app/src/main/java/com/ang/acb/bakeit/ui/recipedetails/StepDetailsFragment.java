package com.ang.acb.bakeit.ui.recipedetails;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.FragmentStepDetailsBinding;

import com.ang.acb.bakeit.ui.common.NavigationController;
import com.ang.acb.bakeit.utils.GlideApp;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

public class StepDetailsFragment extends Fragment  {

    private static final String CURRENT_STEP_COUNT_KEY = "CURRENT_STEP_COUNT_KEY";
    private static final String CURRENT_PLAYBACK_POSITION_KEY = "CURRENT_PLAYBACK_POSITION_KEY";
    private static final String SHOULD_PLAY_WHEN_READY_KEY = "SHOULD_PLAY_WHEN_READY_KEY";
    private static final String EXTRA_IS_TWO_PANE = "EXTRA_IS_TWO_PANE";
    private static final String EXTRA_STEP_POSITION = "EXTRA_STEP_POSITION";

    private FragmentStepDetailsBinding binding;
    private DetailsViewModel viewModel;
    private Integer recipeId;
    private int currentStepCount;
    private int stepPosition;
    private boolean isTwoPane;

    private SimpleExoPlayer simpleExoPlayer;
    private boolean shouldPlayWhenReady;
    private long currentPlaybackPosition;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    // Required empty public constructor
    public StepDetailsFragment() {}

    public static StepDetailsFragment newInstance(Integer recipeId, int stepPosition, boolean isTwoPane) {
        Timber.d("StepDetailsFragment created.");
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
        args.putInt(EXTRA_STEP_POSITION, stepPosition);
        args.putBoolean(EXTRA_IS_TWO_PANE, isTwoPane);
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

        currentStepCount = -1;

        restoreInstanceState(savedInstanceState);
        enableFullscreenMode();
        initViewModel();
        observeSteps();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP_COUNT_KEY, currentStepCount);
        outState.putLong(CURRENT_PLAYBACK_POSITION_KEY, currentPlaybackPosition);
        outState.putBoolean(SHOULD_PLAY_WHEN_READY_KEY, shouldPlayWhenReady);
    }

    private void restoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_STEP_COUNT_KEY)) {
                currentStepCount = savedInstanceState.getInt(CURRENT_STEP_COUNT_KEY);
            }
            if (savedInstanceState.containsKey(CURRENT_PLAYBACK_POSITION_KEY)) {
                currentPlaybackPosition = savedInstanceState.getLong(CURRENT_PLAYBACK_POSITION_KEY);
            }
            if (savedInstanceState.containsKey(SHOULD_PLAY_WHEN_READY_KEY)) {
                shouldPlayWhenReady = savedInstanceState.getBoolean(SHOULD_PLAY_WHEN_READY_KEY);
            }
        }
    }

    private void enableFullscreenMode() {
        if (isFullscreenMode()) {
            // Hide action bar
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).hide();

            // Hide system UI
            // See: https://developer.android.com/training/system-ui/immersive#EnableFullscreen
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    // Enables regular immersive mode.
                    View.SYSTEM_UI_FLAG_IMMERSIVE |
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    // Hide the nav bar and status bar
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private boolean isFullscreenMode() {
        Configuration configuration = getResources().getConfiguration();
        return configuration.smallestScreenWidthDp < 600 &&
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private void initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(DetailsViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getInt(EXTRA_RECIPE_ID);
            stepPosition = args.getInt(EXTRA_STEP_POSITION);
            isTwoPane = args.getBoolean(EXTRA_IS_TWO_PANE);
        }

        viewModel.init(recipeId);
        viewModel.setStepIndexLiveData(stepPosition);
    }

    private void observeSteps(){
        viewModel.getCurrentStep().observe(
                getViewLifecycleOwner(),
                new Observer<Step>() {
                    @Override
                    public void onChanged(Step step) {
                        if (currentStepCount == -1 || currentStepCount != viewModel.getStepCount()) {
                            resetPlayer();
                            currentStepCount = viewModel.getStepCount();
                        }
                        binding.setStepCount(viewModel.getStepCount());
                        binding.setStep(step);
                        handleVideoUrl(step);
                        handleStepButtons();
                    }
                }
        );
    }

    private void handleVideoUrl(Step step){
        // If step has a video, initialize player, else display an image.
        String videoUrl = step.getVideoURL();
        if (!videoUrl.isEmpty()) {
            initializePlayer(Uri.parse(videoUrl));
        } else {
            String thumbnailUrl = step.getThumbnailURL();
            if (!TextUtils.isEmpty(thumbnailUrl)) {
                GlideApp.with(this)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.bakeit)
                        .into(binding.placeholderImage);
            } else {
                binding.placeholderImage.setImageResource(R.drawable.bakeit);
            }
        }
    }


    private void initializePlayer(Uri mediaUri) {
        // See: https://exoplayer.dev/hello-world
        if (simpleExoPlayer == null) {
            // Create the player using the ExoPlayerFactory.
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext());

            // Attach the payer to the view.
            binding.exoplayerView.setPlayer(simpleExoPlayer);
        }

        // Create a media source representing the media to be played.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                Objects.requireNonNull(getContext()),
                Util.getUserAgent(getContext(), getString(R.string.app_name)));
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mediaUri);

        // Prepare the player.
        simpleExoPlayer.prepare(mediaSource);

        // Control the player.
        simpleExoPlayer.seekTo(currentPlaybackPosition);
        simpleExoPlayer.setPlayWhenReady(shouldPlayWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            // Returns the playback position in the current content window
            // or ad, in milliseconds.
            currentPlaybackPosition = simpleExoPlayer.getCurrentPosition();
            // Returns whether playback will proceed when ready (i.e. when
            // Player.getPlaybackState() == Player.STATE_READY.)
            shouldPlayWhenReady = simpleExoPlayer.getPlayWhenReady();

            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    private void resetPlayer() {
        shouldPlayWhenReady = true;
        currentPlaybackPosition = 0;
        if (simpleExoPlayer != null) simpleExoPlayer.stop();
    }

    private void handleStepButtons(){
        if (!isTwoPane && !isFullscreenMode()) {
            // Handle click events
            binding.nextStepButton.setOnClickListener(view -> {
                resetPlayer();
                viewModel.nextStepIndex();
            });
            binding.previousStepButton.setOnClickListener(view -> {
                resetPlayer();
                viewModel.previousStepIndex();
            });
            // Necessary because Espresso cannot read data binding callbacks.
            binding.executePendingBindings();
        }
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
