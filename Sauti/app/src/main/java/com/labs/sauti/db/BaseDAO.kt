package com.labs.sauti.db

import androidx.room.*

@Dao
interface BaseDao<in T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: T): Long

    @Delete
    fun delete(type: T)

    @Update
    fun update(type: T)

}