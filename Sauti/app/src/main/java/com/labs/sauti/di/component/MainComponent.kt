package com.labs.sauti.di.component

import com.labs.sauti.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class])
interface MainComponent {
    fun plus(loginModule: LoginModule): LoginComponent
    fun plus(userModule: UserModule): UserComponent
}