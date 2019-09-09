package com.labs.sauti.activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.labs.sauti.R
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class LanguageActivity : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var africaAnimation: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        findViewById<ImageView>(R.id.i_language_activity).apply {
            setBackgroundResource(R.drawable.animated_africa)
            africaAnimation = background as AnimationDrawable
        }


    }

    override fun onStart() {
        super.onStart()

        africaAnimation.start()


        val intent = Intent(this@LanguageActivity, BaseActivity::class.java)

        compositeDisposable.add(Completable.timer(2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                startActivity(intent)
                finish()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}
