package com.pakachu.fitkal;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class GoToURL {

    public static Intent GoToInstagram() {
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse("https://www.instagram.com/pakachu.s"));
        return openURL;
    }
    public static Intent GoToStore() {
        Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
        openURL.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Pakachu"));
        return openURL;
    }

//    private static InterstitialAd mInterstitialAd;
//    public static void AdLoad(Activity activity) {
//        AdRequest adRequest2 = new AdRequest.Builder().build();
//        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/1033173712", adRequest2,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        mInterstitialAd = interstitialAd;
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        mInterstitialAd = null;
//                    }
//                });
//        if (mInterstitialAd != null) {
//            mInterstitialAd.show(activity);
//        } else {
//        }
//    }


}
