package com.labs.sauti.mapper

import io.reactivex.Observable
import io.reactivex.Single

abstract class Mapper <in T, U> {
    abstract fun mapFrom(from: T): U

    fun observable(from: T): Observable<U> {
        return Observable.fromCallable { mapFrom(from) }
    }

    fun observable(from: List<T>): Observable<List<U>> {
        return Observable.fromCallable { from.map { mapFrom(it) } }
    }

    fun single(from: T): Single<U> {
        return Single.fromCallable { mapFrom(from) }
    }

    fun single(from: List<T>): Single<List<U>> {
        return Single.fromCallable { from.map { mapFrom(it) } }
    }
}