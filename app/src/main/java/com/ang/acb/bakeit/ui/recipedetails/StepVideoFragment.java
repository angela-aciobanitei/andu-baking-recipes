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
import com.ang.acb.bakeit.data.model.WholeRecipe;
import com.ang.acb.bakeit.databinding.FragmentVideoStepBinding;
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

import java.util.Objects;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.ARG_RECIPE_ID;

public class StepVideoFragment extends Fragment  {

    public static final String CURRENT_PLAYBACK_POSITION_TAG = "CURRENT_PLAYBACK_POSITION_TAG";
    public static final String SHOULD_PLAY_WHEN_READY_TAG = "SHOULD_PLAY_WHEN_READY_TAG";
    public static final String CURRENT_STEP_INDEX = "CURRENT_STEP_INDEX";
    public static final String ARG_STEP_ID = "EXTRA_STEP_ID";

    private FragmentVideoStepBinding binding;
    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;
    private Integer stepId;

    private SimpleExoPlayer simpleExoPlayer;
    private boolean shouldPlayWhenReady;
    private long currentPlaybackPosition;

    public StepVideoFragment() {}

    public static StepVideoFragment newInstance(Integer recipeId, Integer stepId) {
        Timber.d("StepVideoFragment created.");
        StepVideoFragment fragment = new StepVideoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_ID, stepId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVideoStepBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Recover the instance state.
        onRestoreInstanceState(savedInstanceState);

        // Enable fullscreen mode for landscape orientation.
        enableFullscreenMode();

        initializeViewModel();

        // TODO Observe step list
        viewModel.getWholeRecipeLiveData().observe(
                getViewLifecycleOwner(),
                new Observer<WholeRecipe>() {
                    @Override
                    public void onChanged(WholeRecipe wholeRecipe) {
                        // Bind step
                        Step step = wholeRecipe.steps.get(stepId);
                        binding.setStep(step);
                        binding.setStepListSize(wholeRecipe.steps.size());

                        handleVideoUrl(step);
                    }
                }
        );

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOULD_PLAY_WHEN_READY_TAG, shouldPlayWhenReady);
        outState.putLong(CURRENT_PLAYBACK_POSITION_TAG, currentPlaybackPosition);
    }

    private void onRestoreInstanceState(Bundle savedInstanceState){
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SHOULD_PLAY_WHEN_READY_TAG)) {
                shouldPlayWhenReady = savedInstanceState.getBoolean(SHOULD_PLAY_WHEN_READY_TAG);
            }
            if (savedInstanceState.containsKey(CURRENT_PLAYBACK_POSITION_TAG)) {
                currentPlaybackPosition = savedInstanceState.getLong(CURRENT_PLAYBACK_POSITION_TAG);
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
        viewModel = ViewModelProviders.of(
                Objects.requireNonNull(getActivity()),factory)
                .get(RecipeDetailsViewModel.class);

        // Get bundle args (with the recipe id) and init recipe details view model.
        Bundle args = getArguments();
        if (args != null) {
            recipeId = args.getInt(ARG_RECIPE_ID);
            stepId = args.getInt(ARG_STEP_ID);
        }

        viewModel.init(recipeId);
        Timber.d("Recipe [id=%s]: init view model for step [id=%s].", recipeId, stepId);
    }

    private void handleStepButtons(){
        // Handle previous and next buttons
        binding.previousStepButton.setOnClickListener(view -> {
            resetVideo();
            // TODO Get previous step id
        });

        binding.nextStepButton.setOnClickListener(view -> {
            resetVideo();
            // TODO Get next step id
        });
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

    private void resetVideo() {
        shouldPlayWhenReady = true;
        currentPlaybackPosition = 0;

        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
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
