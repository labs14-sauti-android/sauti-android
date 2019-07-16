package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* Prelim model. Not final.
* */

@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val productID: Long = 0,
    @ColumnInfo(name = "product_name")
    val name: String
)