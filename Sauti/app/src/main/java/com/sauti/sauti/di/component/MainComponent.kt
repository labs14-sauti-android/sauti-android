package com.sauti.sauti.di.component

import com.sauti.sauti.di.module.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class])
interface MainComponent {
    fun plus(loginModule: LoginModule): LoginComponent
    fun plus(userModule: UserModule): UserComponent
}