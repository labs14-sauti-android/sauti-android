package com.sauti.sauti

import android.app.Application
import com.sauti.sauti.di.component.DaggerMainComponent
import com.sauti.sauti.di.component.LoginComponent
import com.sauti.sauti.di.component.MainComponent
import com.sauti.sauti.di.module.AppModule
import com.sauti.sauti.di.module.DataModule
import com.sauti.sauti.di.module.LoginModule
import com.sauti.sauti.di.module.NetworkModule

class SautiApp : Application() {

    private lateinit var mainComponent: MainComponent
    private var loginComponent: LoginComponent? = null

    override fun onCreate() {
        super.onCreate()

        mainComponent = DaggerMainComponent.builder()
            .appModule(AppModule(applicationContext))
            .networkModule(NetworkModule(getString(R.string.sauti_api_base_url)))
            .dataModule(DataModule(getString(R.string.sauti_login_authorization)))
            .build()
    }

    fun getLoginComponent(): LoginComponent {
        if (loginComponent == null) {
            loginComponent = mainComponent.plus(LoginModule())
        }

        return loginComponent!!
    }
}