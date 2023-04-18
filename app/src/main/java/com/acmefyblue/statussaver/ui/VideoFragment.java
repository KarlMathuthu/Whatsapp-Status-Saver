package com.acmefyblue.statussaver.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.acmefyblue.statussaver.BtnClick2;
import com.acmefyblue.statussaver.R;
import com.acmefyblue.statussaver.adapter.VideoAdapter;
import com.acmefyblue.statussaver.model.Status;
import com.acmefyblue.statussaver.utils.Common;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VideoFragment extends Fragment implements IUnityAdsInitializationListener {


    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private final List<Status> videoList = new ArrayList<>();
    private final Handler handler = new Handler();
    private VideoAdapter videoAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView messageTextView;

    private String unityGameID = "5243794";
    private Boolean testMode = false;
    private String adUnitId = "VideoFragment_Rewarded";

    private IUnityAdsLoadListener loadListener = new IUnityAdsLoadListener() {
        @Override
        public void onUnityAdsAdLoaded(String placementId) {
            UnityAds.show((Activity)getActivity(), adUnitId, new UnityAdsShowOptions(), showListener);
        }

        @Override
        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to load ad for " + placementId + " with error: [" + error + "] " + message);
        }
    };

    private IUnityAdsShowListener showListener = new IUnityAdsShowListener() {
        @Override
        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
            Log.e("UnityAdsExample", "Unity Ads failed to show ad for " + placementId + " with error: [" + error + "] " + message);
        }

        @Override
        public void onUnityAdsShowStart(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowStart: " + placementId);
        }

        @Override
        public void onUnityAdsShowClick(String placementId) {
            Log.v("UnityAdsExample", "onUnityAdsShowClick: " + placementId);
        }

        @Override
        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
            Log.v("UnityAdsExample", "onUnityAdsShowComplete: " + placementId);
            if (state.equals(UnityAds.UnityAdsShowCompletionState.COMPLETED)) {
                // Reward the user for watching the ad to completion
            } else {
                // Do not reward the user for skipping the ad
            }
        }
    };




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        recyclerView = view.findViewById(R.id.recyclerViewVideo);
        progressBar = view.findViewById(R.id.prgressBarVideo);
        RelativeLayout container = view.findViewById(R.id.videos_container);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        messageTextView = view.findViewById(R.id.messageTextVideo);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(requireActivity(), R.color.green1)
                , ContextCompat.getColor(requireActivity(), R.color.green1),
                ContextCompat.getColor(requireActivity(), R.color.green1),
                ContextCompat.getColor(requireActivity(), R.color.green1));

        swipeRefreshLayout.setOnRefreshListener(this::getStatus);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), Common.GRID_COUNT));

        getStatus();

        super.onViewCreated(view, savedInstanceState);
    }

    private void getStatus() {

        if (Common.STATUS_DIRECTORY.exists()) {

            new Thread(() -> {
                File[] statusFiles = Common.STATUS_DIRECTORY.listFiles();
                videoList.clear();

                if (statusFiles != null && statusFiles.length > 0) {

                    Arrays.sort(statusFiles);
                    for (File file : statusFiles) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());

                        if (status.isVideo()) {
                            videoList.add(status);
                        }

                    }

                    handler.post(() -> {

                        if (videoList.size() <= 0) {
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText(R.string.no_files_found);
                        } else {
                            messageTextView.setVisibility(View.GONE);
                            messageTextView.setText("");
                        }

                        videoAdapter = new VideoAdapter(videoList, new BtnClick2() {
                            @Override
                            public void btn_click2() {
                                //Rewarded Ad
                                DisplayRewardedAd();

                            }
                        });
                        recyclerView.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    });

                } else {

                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        messageTextView.setVisibility(View.VISIBLE);
                        messageTextView.setText(R.string.no_files_found);
                    });

                }
                swipeRefreshLayout.setRefreshing(false);
            }).start();

        } else if(Common.STATUS_DIRECTORY2.exists()){
            new Thread(() -> {
                File[] statusFiles = Common.STATUS_DIRECTORY2.listFiles();
                videoList.clear();

                if (statusFiles != null && statusFiles.length > 0) {

                    Arrays.sort(statusFiles);
                    for (File file : statusFiles) {
                        Status status = new Status(file, file.getName(), file.getAbsolutePath());

                        if (status.isVideo()) {
                            videoList.add(status);
                        }

                    }

                    handler.post(() -> {

                        if (videoList.size() <= 0) {
                            messageTextView.setVisibility(View.VISIBLE);
                            messageTextView.setText(R.string.no_files_found);
                        } else {
                            messageTextView.setVisibility(View.GONE);
                            messageTextView.setText("");
                        }

                        videoAdapter = new VideoAdapter(videoList, new BtnClick2() {
                            @Override
                            public void btn_click2() {
                                //Rewarded Ad
                                DisplayRewardedAd();

                            }
                        });
                        recyclerView.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    });

                } else {

                    handler.post(() -> {
                        progressBar.setVisibility(View.GONE);
                        messageTextView.setVisibility(View.VISIBLE);
                        messageTextView.setText(R.string.no_files_found);
                    });

                }
                swipeRefreshLayout.setRefreshing(false);
            }).start();

        }else{
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(R.string.cant_find_whatsapp_dir);
            swipeRefreshLayout.setRefreshing(false);
        }

    }
    @Override
    public void onInitializationComplete() {

    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

    }
    // Implement a function to load a rewarded ad. The ad will start to show after the ad has been loaded.
    public void DisplayRewardedAd () {
        UnityAds.load(adUnitId, loadListener);
    }

}
