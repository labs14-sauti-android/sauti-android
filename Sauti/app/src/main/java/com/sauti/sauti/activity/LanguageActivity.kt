package com.sauti.sauti.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sauti.sauti.R

class LanguageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        val languages = arrayOf("English", "Swahili", "Luganda")
    }
}
