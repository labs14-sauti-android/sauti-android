package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.labs.sauti.R

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bundle = Bundle()
        bundle.putString("app_open", "Someone opened the app")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)

        val intent = Intent(this, LanguageActivity::class.java)
        startActivity(intent)

        finish()

    }

}
