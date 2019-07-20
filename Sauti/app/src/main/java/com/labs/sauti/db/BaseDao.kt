package com.labs.sauti.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Maybe

@Dao
interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: T): Maybe<Long>

    @Delete
    fun delete(type: T): Completable

    @Update
    fun update(type: T): Completable

}