package com.example.bakingapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.bakingapp.MainActivity;
import com.example.bakingapp.R;
import com.example.bakingapp.activities.RecipeDetailActivity;
import com.example.bakingapp.activities.StepDetailActivity;
import com.example.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.exoplayer2.Player.*;

public class StepDetailFragment extends Fragment implements Player.EventListener, SimpleExoPlayer.VideoListener {

    private Step mCurrentStep;
    private ArrayList<Step> mCurrentSteps;
    private Context mContext;
    private SimpleExoPlayer mExoplayer;

    @BindView(R.id.exop_step_video_view)
    public SimpleExoPlayerView mExoplayerView;
    @BindView(R.id.tv_step_description)
    public TextView mDescription;
    @BindView(R.id.btn_previous_video)
    public Button mPrevBtn;
    @BindView(R.id.btn_next_video)
    public Button mNextBtn;
    @BindView(R.id.tv_no_video)
    public TextView mNoVideo;
    public boolean mIsLandscape;
    @BindView(R.id.tv_loading_video)
    public ProgressBar mLoadingVideo;
    public boolean mIsMasterDetailLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail_container, container, false);
        ButterKnife.bind(this, rootView);

        int currentOrientation = getResources().getConfiguration().orientation;
        mIsLandscape = (currentOrientation == Configuration.ORIENTATION_LANDSCAPE);

        mIsMasterDetailLayout = setMasterDetailLayout();

        int position = setStepPosition(savedInstanceState);
        if(mIsMasterDetailLayout) {
            getDataFromRecipeDetailActivity(position);
        } else {
            getDataFromStepDetailActivity(position, savedInstanceState);
        }

        mDescription.setText(mCurrentStep.getDescription());
        initStepControlButtons(rootView);
        initializeVideoPlayer(mCurrentStep.getVideoUrl());

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void getDataFromStepDetailActivity(int position, @Nullable Bundle savedInstanceState) {
        StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();
        mCurrentSteps = stepDetailActivity.getSteps();
        if(savedInstanceState == null) {
            mCurrentStep = stepDetailActivity.getStep();
        } else {
            mCurrentStep = mCurrentSteps.get(position);
        }
        if(mIsLandscape) {
            //hide appbar
            stepDetailActivity.hideActionBar();
        }
    }

    public void getDataFromRecipeDetailActivity(int position) {
        RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
        mCurrentSteps = recipeDetailActivity.getSteps();
        mCurrentStep = mCurrentSteps.get(position);
    }

    public void initializeVideoPlayer(String videoUrl) {
        mExoplayerView.setVisibility(View.GONE);
        if(videoUrl == null || videoUrl.isEmpty()) {
            mNoVideo.setVisibility(View.VISIBLE);
            return;
        } else {
            //mExoplayerView.setVisibility(View.VISIBLE);
            mNoVideo.setVisibility(View.INVISIBLE);
        }

        int stepsSize = mCurrentSteps.size() - 1;
        int nextId = mCurrentStep.getStepId();
        setButtonsState(stepsSize, nextId);

        if(mExoplayer == null) {
            mLoadingVideo.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(videoUrl);
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mExoplayerView.setPlayer(mExoplayer);

            String userAgent = Util.getUserAgent(mContext,  mContext.getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(mContext, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(true);
            //mExoplayer.addVideoListener(this);
            mExoplayer.addListener(this);

        }
    }

    private void initStepControlButtons(View rootView) {
        mPrevBtn.setEnabled(false);
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextVideo("previous");
            }
        });

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextVideo("next");
            }
        });
    }

    public void playNextVideo(String action) {
        int stepsSize = mCurrentSteps.size() - 1;
        int currentId = mCurrentStep.getStepId();
        int nextId;


        if(action == "next"){
            nextId = currentId + 1;
        } else {
            nextId = currentId - 1;
        }

        setButtonsState(stepsSize, nextId);

        mCurrentStep = mCurrentSteps.get(nextId);
        if(mIsMasterDetailLayout) {
            RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
            recipeDetailActivity.setTitleBarText(mCurrentStep.getShortDescription());
        } else {
            StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();
            stepDetailActivity.setTitleBarText(mCurrentStep.getShortDescription());
        }

        mDescription.setText(mCurrentStep.getDescription());
        releasePlayer();
        initializeVideoPlayer(mCurrentStep.getVideoUrl());
    }

    public int setStepPosition( @Nullable Bundle savedInstanceState) {
        int position;
        Bundle bundle = this.getArguments();
        System.out.println("savedInstanceState from StepDetailFragment" + savedInstanceState);
        System.out.println("bundle " + bundle);
        if(savedInstanceState!= null) {
            System.out.println("savedinstance state if");
            position =savedInstanceState.getInt("position");
        } else if (bundle != null) {
            System.out.println("bundle if");
            position = bundle.getInt("position", 0);
        } else {
            System.out.println("else");
            position = 0;
        }

        return position;
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }
    }

    private void setButtonsState(int stepsSize, int nextId) {
        if(nextId == 0) {
            mPrevBtn.setEnabled(false);
        } else {
            mPrevBtn.setEnabled(true);
        }

        if(stepsSize == nextId) {
            mNextBtn.setEnabled(false);
        } else {
            mNextBtn.setEnabled(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == STATE_READY) {
            //do something
            mLoadingVideo.setVisibility(View.INVISIBLE);
            mExoplayerView.setVisibility(View.VISIBLE);
        }
    }

    public boolean setMasterDetailLayout() {
        boolean isMasterDetailLayout = false;
        if( getActivity().getClass().getSimpleName().equals("RecipeDetailActivity")) {
            RecipeDetailActivity recipeDetailActivity = (RecipeDetailActivity) getActivity();
            if(recipeDetailActivity.getFragmentDivider() != null) {
                isMasterDetailLayout = true;
            }
        }

        return isMasterDetailLayout;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mCurrentStep.getStepId());
    }
}
