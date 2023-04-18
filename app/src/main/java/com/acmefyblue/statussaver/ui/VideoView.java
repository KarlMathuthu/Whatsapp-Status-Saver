package com.acmefyblue.statussaver.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acmefyblue.statussaver.R;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

import java.io.File;

public class VideoView extends AppCompatActivity implements IUnityAdsInitializationListener {
    MediaController mediaControls;
    android.widget.VideoView videoView;

    String unityGameID = "5243794";
    Boolean testMode = false;
    String bottomAdUnitId = "Banner_Android";

    // Listener for banner events:
    private BannerView.IListener bannerListener = new BannerView.IListener() {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            // Called when the banner is loaded.
            Log.v("UnityAdsExample", "onBannerLoaded: " + bannerAdView.getPlacementId());
            // Enable the correct button to hide the ad
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            Log.e("UnityAdsExample", "Unity Ads failed to load banner for " + bannerAdView.getPlacementId() + " with error: [" + errorInfo.errorCode + "] " + errorInfo.errorMessage);
            // Note that the BannerErrorInfo object can indicate a no fill (refer to the API documentation).
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            // Called when a banner is clicked.
            Log.v("UnityAdsExample", "onBannerClick: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            // Called when the banner links out of the application.
            Log.v("UnityAdsExample", "onBannerLeftApplication: " + bannerAdView.getPlacementId());
        }
    };
    LinearLayout bottomBannerView;
    // This banner view object will be placed at the bottom of the screen:
    BannerView bottomBanner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        UnityAds.initialize(VideoView.this, unityGameID, testMode, this);
        // Create the bottom banner view object:
        bottomBanner = new BannerView(VideoView.this, bottomAdUnitId, new UnityBannerSize(320, 50));
        // Set the listener for banner lifecycle events:
        bottomBanner.setListener(bannerListener);
        bottomBannerView = findViewById(R.id.video_view_banner);
        LoadBannerAd(bottomBanner, bottomBannerView);

        videoView = findViewById(R.id.video_view);
        Intent intent = getIntent();
        String file1 = intent.getStringExtra("file");
        File file = new File(file1);

        ImageButton img_btn = findViewById(R.id.imageButton);
        img_btn.setOnClickListener(view -> finish());


        videoView.setVideoURI(Uri.parse(String.valueOf(file)));
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoView.this);
            mediaControls.setAnchorView(videoView);
        }

        videoView.setMediaController(mediaControls);
        videoView.setVideoURI(Uri.parse(String.valueOf(file)));

        videoView.start();

        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show();

            return false;
        });
    }
    public void LoadBannerAd(BannerView bannerView, LinearLayout bannerLayout) {
        // Request a banner ad:
        bannerView.load();
        // Associate the banner view object with the banner view:
        bannerLayout.addView(bannerView);
    }


    @Override
    public void onInitializationComplete() {
        Log.d("ADS","BANNER initialized!");
    }

    @Override
    public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {

    }

}