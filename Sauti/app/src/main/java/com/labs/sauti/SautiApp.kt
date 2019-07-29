package com.labs.sauti

import android.app.Application
import com.labs.sauti.di.component.*
import com.labs.sauti.di.module.*

class SautiApp : Application() {

    private lateinit var mainComponent: MainComponent
    private var authenticationComponent: AuthenticationComponent? = null
    private var marketPriceComponent: MarketPriceComponent? = null

    override fun onCreate() {
        super.onCreate()

        mainComponent = DaggerMainComponent.builder()
            .appModule(AppModule(applicationContext))
            .dataModule(DataModule(getString(R.string.sauti_login_authorization)))
            .build()
    }

    fun getMainComponent() = mainComponent

    fun getAuthenticationComponent(): AuthenticationComponent {
        if (authenticationComponent == null) {
            authenticationComponent = mainComponent.plus(AuthenticationModule())
        }

        return authenticationComponent!!
    }

    fun getMarketPriceComponent(): MarketPriceComponent {
        if (marketPriceComponent == null) {
            marketPriceComponent = mainComponent.plus(MarketPriceModule())
        }

        return marketPriceComponent!!
    }
}