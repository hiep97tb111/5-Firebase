package com.example.firebasedemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // test crashlytics
        eventCrashlytics()

        // log event
        findViewById<TextView>(R.id.tvLogEvent).setOnClickListener {
            handleLogEvent()
        }

        // show ads banner
        showAdsBanner()

        // remote config
        findViewById<Button>(R.id.btnRemoteConfig).setOnClickListener {
            showRemoteConfig()
        }

    }

    private fun showRemoteConfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        val tvRemoteConfig = findViewById<TextView>(R.id.tvRemoteConfig)

        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val updated: Boolean = it.result
                Log.d("Logger", "Config params updated: $updated")
            } else {
                Log.d("Logger", "Fetch failed")
            }
        }

        val welcomeMessage = mFirebaseRemoteConfig.getString("welcome_message")
        Log.e("Logger", welcomeMessage)
        tvRemoteConfig.text = welcomeMessage

    }

    private fun showAdsBanner() {
        val adsBanner = findViewById<AdView>(R.id.adsBanner)
        val adsRequest = AdRequest.Builder()
            .build()
        adsBanner.loadAd(adsRequest)

        adsBanner.adListener = object: AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.e("Logger", "onAdClicked")
            }
            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.e("Logger", "onAdClosed")
            }
            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.e("Logger", "onAdFailedToLoad: ${adError.responseInfo} ${adError.message}")
            }
            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
                Log.e("Logger", "onAdImpression")
            }
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.e("Logger", "onAdLoaded")
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.e("Logger", "onAdOpened")
            }
        }

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adsRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("Logger Fail", adError?.toString())
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("Logger Success", interstitialAd.responseInfo.toString())
            }
        })
    }

    private fun handleLogEvent() {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle()
        bundle.putString("key", "Manchester United");
        firebaseAnalytics.logEvent("footballClub", bundle)
        Log.e("Logger", "Manchester United")
    }

    @SuppressLint("SetTextI18n")
    private fun eventCrashlytics() {
        val crashButton = Button(this)
        crashButton.text = "Test Crash"
        crashButton.setOnClickListener {
            throw RuntimeException("Test Crash") // Force a crash
        }

        addContentView(crashButton, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT))
    }
}