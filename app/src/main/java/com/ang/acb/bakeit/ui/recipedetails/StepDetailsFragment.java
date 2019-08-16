package com.ang.acb.bakeit.ui.recipedetails;

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
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.data.model.Step;
import com.ang.acb.bakeit.databinding.FragmentStepDetailsBinding;

import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.RecipeListActivity.ARG_RECIPE_ID;

public class StepDetailsFragment extends Fragment  {

    public static final String CURRENT_STEP_POSITION_KEY = "CURRENT_STEP_POSITION_KEY";
    public static final String CURRENT_PLAYBACK_POSITION_KEY = "CURRENT_PLAYBACK_POSITION_KEY";
    public static final String SHOULD_PLAY_WHEN_READY_KEY = "SHOULD_PLAY_WHEN_READY_KEY";
    public static final String ARG_STEP_ID = "EXTRA_STEP_ID";

    private FragmentStepDetailsBinding binding;
    private StepDetailsViewModel viewModel;
    private Integer recipeId;
    private int currentStepPosition;

    private SimpleExoPlayer simpleExoPlayer;
    private boolean shouldPlayWhenReady;
    private long currentPlaybackPosition;

    public StepDetailsFragment() {}

    public static StepDetailsFragment newInstance(Integer recipeId) {
        Timber.d("StepDetailsFragment created.");
        StepDetailsFragment fragment = new StepDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);

        return fragment;
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

        restoreInstanceState(savedInstanceState);
        enableFullscreenMode();
        initializeViewModel();
        observeStepList();
        handleStepButtons();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STEP_POSITION_KEY, currentStepPosition);
        outState.putLong(CURRENT_PLAYBACK_POSITION_KEY, currentPlaybackPosition);
        outState.putBoolean(SHOULD_PLAY_WHEN_READY_KEY, shouldPlayWhenReady);
    }

    private void restoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_STEP_POSITION_KEY)) {
                currentStepPosition = savedInstanceState.getInt(CURRENT_STEP_POSITION_KEY);
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
        Configuration configuration = Objects.requireNonNull(
                getContext()).getResources().getConfiguration();

        boolean isFullscreenMode =
                configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                && configuration.smallestScreenWidthDp < 600;

        if (isFullscreenMode) {
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

    private void initializeViewModel() {
        // Create view model.
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory)
                .get(StepDetailsViewModel.class);

        // Get bundle args (with the recipe id) and init recipe details view model.
        Bundle args = getArguments();
        if (args != null) recipeId = args.getInt(ARG_RECIPE_ID);


        viewModel.init(recipeId, currentStepPosition);
        Timber.d("Recipe [id=%s]: init view model for step [position=%s].",
                recipeId, currentStepPosition);
    }

    private void observeStepList(){
        // TODO Observe step list
        viewModel.getStepsLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<List<Step>>() {
                    @Override
                    public void onChanged(List<Step> steps) {
                        // Bind step
                        Step step = steps.get(currentStepPosition);
                        binding.setStep(step);
                        binding.setStepListSize(steps.size());

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
                Picasso.get()
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
            // Returns the playback position in the current content window or ad, in milliseconds.
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

        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
        }
    }

    private void handleStepButtons(){
        binding.previousStepButton.setOnClickListener(view -> {
            resetPlayer();
            // TODO Get prev step
            viewModel.previousStep();
        });

        binding.nextStepButton.setOnClickListener(view -> {
            resetPlayer();
            // TODO Get next step
            viewModel.nextStep();
        });
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
