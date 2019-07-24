package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.labs.sauti.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_language.*
import java.util.concurrent.TimeUnit

class LanguageActivity : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)


/*        ArrayAdapter.createFromResource(this,
            R.array.languages_array,
            android.R.layout.simple_spinner_item
        ).also { adapter->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            select_language_spinner.adapter = adapter
        }

        button.setOnClickListener {
            // TODO save selected language
            val intent = Intent(this@LanguageActivity, DashboardActivity::class.java)
            startActivity(intent)

            finish()
        }*/

        val intent = Intent(this@LanguageActivity, BaseActivity::class.java)

        compositeDisposable.add(Completable.timer(1500, TimeUnit.MILLISECONDS)
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
