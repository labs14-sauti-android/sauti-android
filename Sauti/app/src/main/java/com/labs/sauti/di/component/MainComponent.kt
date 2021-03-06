package com.labs.sauti.di.component

import com.labs.sauti.activity.LanguageActivity
import com.labs.sauti.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class, GsonModule::class])
interface MainComponent {
    fun plus(authenticationModule: AuthenticationModule): AuthenticationComponent
    fun plus(marketPriceModule: MarketPriceModule): MarketPriceComponent
    fun plus(taxCalculatorModule: TaxCalculatorModule): TaxCalculatorComponent
    fun plus(exchangeRateModule: ExchangeRateModule): ExchangeRateComponent
    fun plus(settingsModule: SettingsModule): SettingsComponent
    fun plus(tradeInfoModule: TradeInfoModule) : TradeInfoComponent
    fun plus(helpModule: HelpModule): HelpComponent
    fun plus(reportModule: ReportModule): ReportComponent
    fun plus(dashboardModule: DashboardModule): DashboardComponent
    fun inject(languageActivity: LanguageActivity)
}