package com.example.bakingapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.activities.StepDetailActivity;
import com.example.bakingapp.models.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
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

public class StepDetailFragment extends Fragment implements SimpleExoPlayer.VideoListener {

    private Step mCurrentStep;
    private ArrayList<Step> mCurrentSteps;
    private Context mContext;
    private SimpleExoPlayer mExoplayer;
    private SimpleExoPlayerView mExoplayerView;
    private TextView mDescription;
    private Button mPrevBtn;
    private Button mNextBtn;
    private TextView mNoVideo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_detail_container, container, false);

        getDataFromContainerActivity();
        mExoplayerView = rootView.findViewById(R.id.exop_step_video_view);
        ///
        mNoVideo = rootView.findViewById(R.id.tv_no_video);
        mDescription = rootView.findViewById(R.id.tv_step_description);
        mDescription.setText(mCurrentStep.getDescription());
        //mDescription.getLayoutParams().width = 100;
        ///
        initializeVideoPlayer(mCurrentStep.getVideoUrl());
        initStepControlButtons(rootView);
        return rootView;
    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        mDescription.getLayoutParams().width = width;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void getDataFromContainerActivity() {
        StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();
        mCurrentStep = stepDetailActivity.getStep();
        mCurrentSteps = stepDetailActivity.getSteps();
    }

    public void initializeVideoPlayer(String videoUrl) {
        if(videoUrl == null || videoUrl.isEmpty()) {
            mExoplayerView.setVisibility(View.INVISIBLE);
            mNoVideo.setVisibility(View.VISIBLE);
            return;
        } else {
            mExoplayerView.setVisibility(View.VISIBLE);
            mNoVideo.setVisibility(View.INVISIBLE);
        }

        if(mExoplayer == null) {
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
        }
    }

    private void initStepControlButtons(View rootView) {
        mPrevBtn = rootView.findViewById(R.id.btn_previous_video);
        mPrevBtn.setEnabled(false);
        mPrevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNextVideo("previous");
            }
        });

        mNextBtn = rootView.findViewById(R.id.btn_next_video);
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

        System.out.println(nextId);
        System.out.println(stepsSize);


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


        mCurrentStep = mCurrentSteps.get(nextId);
        StepDetailActivity stepDetailActivity = (StepDetailActivity) getActivity();
        stepDetailActivity.setTitleBarText(mCurrentStep.getShortDescription());

        mDescription.setText(mCurrentStep.getDescription());
        releasePlayer();
        initializeVideoPlayer(mCurrentStep.getVideoUrl());
    }

    private void releasePlayer() {
        if (mExoplayer != null) {
            mExoplayer.stop();
            mExoplayer.release();
            mExoplayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
