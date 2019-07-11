package com.sauti.sauti.di.component

import com.sauti.sauti.di.module.AppModule
import com.sauti.sauti.di.module.DataModule
import com.sauti.sauti.di.module.LoginModule
import com.sauti.sauti.di.module.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class])
interface MainComponent {
    fun plus(loginModule: LoginModule): LoginComponent
}