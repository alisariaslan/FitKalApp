package com.pakachu.fitkal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Timer;
import java.util.TimerTask;

public class AddActivity extends AppCompatActivity {
    public static boolean add = true;
    private InterstitialAd mInterstitialAd;
private int time=60000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

       new CountDownTimer(60000,1000)  {

           @Override
           public void onTick(long l) {
               Log.e("timer",""+l);
           }

           @Override
           public void onFinish() {
               AdRequest adRequest2 = new AdRequest.Builder().build();
               InterstitialAd.load(AddActivity.this, getResources().getString(R.string.interstatialAds), adRequest2,
                       new InterstitialAdLoadCallback() {
                           @Override
                           public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                               mInterstitialAd = interstitialAd;
                               if (mInterstitialAd != null) {
                                   mInterstitialAd.show(AddActivity.this);

                               }
                           }

                           @Override
                           public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                               mInterstitialAd = null;
                           }
                       });
           }
       }.start();


    }

    @Override
    protected void onResume() {
        super.onResume();

        startActivity(new Intent(this, MainActivity.class));
    }
}