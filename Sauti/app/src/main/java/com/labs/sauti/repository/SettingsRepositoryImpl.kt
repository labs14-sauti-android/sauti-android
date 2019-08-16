package com.labs.sauti.repository

import com.labs.sauti.sp.SettingsSp
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SettingsRepositoryImpl(
    private val settingsSp: SettingsSp
): SettingsRepository{
    override fun getSelectedLanguage(): Single<String> {
        return Single.just(settingsSp.getSelectedLanguage())
            .subscribeOn(Schedulers.io())
    }

    override fun setSelectedLanguage(language: String): Completable {
        return Completable.fromCallable {
            settingsSp.setSelectedLanguage(language)
        }
    }
}