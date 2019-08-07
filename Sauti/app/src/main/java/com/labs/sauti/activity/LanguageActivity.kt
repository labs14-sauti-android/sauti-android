package com.labs.sauti.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.labs.sauti.R
import com.labs.sauti.sp.SettingsSp
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.*
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

        val settingsSp = SettingsSp(this)
        val locale = Locale(settingsSp.getSelectedLanguage())
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        val intent = Intent(this@LanguageActivity, BaseActivity::class.java)

        compositeDisposable.add(Completable.timer(1500, TimeUnit.MILLISECONDS)
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
