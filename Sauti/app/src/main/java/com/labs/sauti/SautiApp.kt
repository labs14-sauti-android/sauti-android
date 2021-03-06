package com.labs.sauti

import android.app.Application
import com.labs.sauti.di.component.*
import com.labs.sauti.di.module.*

class SautiApp : Application() {

    private lateinit var mainComponent: MainComponent
    private var authenticationComponent: AuthenticationComponent? = null
    private var marketPriceComponent: MarketPriceComponent? = null
    private var taxCalculatorComponent: TaxCalculatorComponent? = null
    private var exchangeRateComponent: ExchangeRateComponent? = null
    private var settingsComponent: SettingsComponent? = null
    private var tradeinfoComponent : TradeInfoComponent? = null
    private var helpComponent: HelpComponent? = null
    private var reportComponent: ReportComponent? = null
    val dashboardComponent by lazy { mainComponent.plus(DashboardModule()) }

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

    fun getExchangeRateComponent(): ExchangeRateComponent {
        if (exchangeRateComponent == null) {
            exchangeRateComponent = mainComponent.plus(ExchangeRateModule())
        }

        return exchangeRateComponent!!
    }

    fun getSettingsComponent(): SettingsComponent {
        if (settingsComponent == null) {
            settingsComponent = mainComponent.plus(SettingsModule())
        }

        return settingsComponent!!
    }

    fun getTradeInfoComponent() : TradeInfoComponent {
        if(tradeinfoComponent == null ) {
            tradeinfoComponent = mainComponent.plus(TradeInfoModule())
        }
        return tradeinfoComponent!!
    }

    fun getTaxCalculatorComponent() : TaxCalculatorComponent {
        if(taxCalculatorComponent == null ) {
            taxCalculatorComponent = mainComponent.plus(TaxCalculatorModule())
        }
        return taxCalculatorComponent!!
    }

    fun getHelpComponent() : HelpComponent {
        if (helpComponent == null) {
            helpComponent = mainComponent.plus(HelpModule())
        }
        return helpComponent!!
    }

    fun getReportComponent(): ReportComponent {
        if (reportComponent == null) {
            reportComponent = mainComponent.plus(ReportModule())
        }
        return reportComponent!!
    }
}