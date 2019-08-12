package com.labs.sauti.repository

import io.reactivex.Completable

interface HelpRepository {
    fun submitIncorrectInformation(incorrectInformation: String): Completable
}