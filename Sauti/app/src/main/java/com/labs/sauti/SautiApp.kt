package com.labs.sauti

import android.app.Application
import com.labs.sauti.di.component.DaggerMainComponent
import com.labs.sauti.di.component.LoginComponent
import com.labs.sauti.di.component.MainComponent
import com.labs.sauti.di.component.UserComponent
import com.labs.sauti.di.module.*

class SautiApp : Application() {

    private lateinit var mainComponent: MainComponent
    private var loginComponent: LoginComponent? = null
    private var userComponent: UserComponent? = null

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

    fun getUserComponent(): UserComponent {
        if (userComponent == null) {
            userComponent = mainComponent.plus(UserModule())
        }

        return userComponent!!
    }
}