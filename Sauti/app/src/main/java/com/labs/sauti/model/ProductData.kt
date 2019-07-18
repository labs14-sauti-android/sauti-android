package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/*
* Prelim model. Not final.
* Should it include price in User's main currency and conversions?
*
* Should currency be even placed into this model?
* */


@Entity(tableName = "products")
data class ProductData (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val productID: Long = 0,
    @ColumnInfo(name = "product_name")
    val name: String,
    //Does the user even care about category or is this a way for
    //us to neatly present data.
    @ColumnInfo(name = "product_cat")
    var productCat: String,
    @ColumnInfo(name = "price_wholesale")
    val wholesale : Int,
    @ColumnInfo(name = "price_retail")
    val retail : Int

)