package com.labs.sauti.db

import androidx.room.*
import io.reactivex.Completable

@Dao
interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: T): Completable

    @Delete
    fun delete(type: T): Completable

    @Update
    fun update(type: T): Completable

}