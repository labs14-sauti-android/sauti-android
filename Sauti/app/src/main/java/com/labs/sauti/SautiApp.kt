package com.labs.sauti

import android.app.Application
import com.labs.sauti.di.component.*
import com.labs.sauti.di.module.*

class SautiApp : Application() {

    private lateinit var mainComponent: MainComponent
    private var loginComponent: LoginComponent? = null
    private var marketPricesComponent: MarketPricesComponent? = null

    override fun onCreate() {
        super.onCreate()

        mainComponent = DaggerMainComponent.builder()
            .appModule(AppModule(applicationContext))
            .networkModule(NetworkModule(getString(R.string.sauti_api_base_url)))
            .dataModule(DataModule(getString(R.string.sauti_login_authorization)))
            .build()
    }

    fun getMainComponent() = mainComponent

    fun getLoginComponent(): LoginComponent {
        if (loginComponent == null) {
            loginComponent = mainComponent.plus(LoginModule())
        }

        return loginComponent!!
    }

    fun getMarketPricesComponent(): MarketPricesComponent {
        if (marketPricesComponent == null) {
            marketPricesComponent = mainComponent.plus(MarketPricesModule())
        }

        return marketPricesComponent!!
    }
}