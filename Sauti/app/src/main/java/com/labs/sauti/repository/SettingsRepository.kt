package com.labs.sauti.repository

import io.reactivex.Completable
import io.reactivex.Single

interface SettingsRepository {

    fun getSelectedLanguage(): Single<String>
    fun setSelectedLanguage(language: String): Completable

}