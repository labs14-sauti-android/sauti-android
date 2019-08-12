package com.labs.sauti.repository

import com.labs.sauti.api.SautiApiService
import io.reactivex.Completable

class HelpRepositoryImpl(sautiApiService: SautiApiService): HelpRepository {
    override fun submitIncorrectInformation(incorrectInformation: String): Completable {
        return Completable.complete()
    }

}