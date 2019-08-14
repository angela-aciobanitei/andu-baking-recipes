package com.ang.acb.bakeit.ui.recipedetails;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ang.acb.bakeit.R;
import com.ang.acb.bakeit.databinding.FragmentVideoStepBinding;
import com.ang.acb.bakeit.utils.InjectorUtils;
import com.ang.acb.bakeit.utils.ViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import timber.log.Timber;

import static com.ang.acb.bakeit.ui.recipelist.MainActivity.EXTRA_RECIPE_ID;

public class StepVideoFragment extends Fragment {

    private FragmentVideoStepBinding binding;
    private SimpleExoPlayer simpleExoPlayer;
    private RecipeDetailsViewModel viewModel;
    private Integer recipeId;
    private boolean shouldPlayVideo;
    private long prevStepPosition;

    public StepVideoFragment() {}

    public static StepVideoFragment newInstance(Integer recipeId) {
        Timber.d("StepVideoFragment created.");
        StepVideoFragment fragment = new StepVideoFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID, recipeId);
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

        // Create view model
        ViewModelFactory factory = InjectorUtils.provideViewModelFactory(getContext());
        viewModel = ViewModelProviders.of(getActivity(),factory)
                .get(RecipeDetailsViewModel.class);

        // Get bundle args (with thw recipe id) and init recipe details view model.
        Bundle args = getArguments();
        if (args != null) recipeId = args.getInt(EXTRA_RECIPE_ID);

        viewModel.init(recipeId);
        Timber.d("Recipe [id=%s]: init view model.", recipeId);

    }

    /**
     * Initialize the player.
     * See: https://exoplayer.dev/hello-world
     *
     * @param mediaUri The URI of the media to be played.
     */
    private void initializePlayer(Uri mediaUri) {
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

        // Control the player. Once the player has been prepared, playback
        // can be controlled by calling methods on the player. For example
        // setPlayWhenReady starts and pauses playback, the various seekTo
        // methods seek within the media
        simpleExoPlayer.seekTo(prevStepPosition);
        simpleExoPlayer.setPlayWhenReady(shouldPlayVideo);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            prevStepPosition = simpleExoPlayer.getCurrentPosition();
            shouldPlayVideo = simpleExoPlayer.getPlayWhenReady();

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


}
