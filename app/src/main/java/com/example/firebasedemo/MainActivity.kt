package com.example.firebasedemo

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics

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