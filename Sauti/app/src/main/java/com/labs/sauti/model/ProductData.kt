package com.labs.sauti.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/*
* Prelim model. Not final.
* Should it include price in User's main currency and conversions?
*
* Should currency be even placed into this model?
*
* Might not need but altered for now.
* */


@Entity(tableName = "products")
data class ProductData (

    @SerializedName("productid")
    @Expose
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val productID: Long = 0,

    @SerializedName("productname")
    @Expose
    @ColumnInfo(name = "product_name")
    val name: String? = null,

    //Does the user even care about category or is this a way for
    //us to neatly present data.
    @SerializedName("productcategory")
    @Expose
    @ColumnInfo(name = "product_cat")
    var productCat: String? = null,

    @SerializedName("wholesaleprice")
    @Expose
    @ColumnInfo(name = "price_wholesale")
    val wholesale : Int? = null,

    @SerializedName("retailprice")
    @Expose
    @ColumnInfo(name = "price_retail")
    val retail : Int? = null

)