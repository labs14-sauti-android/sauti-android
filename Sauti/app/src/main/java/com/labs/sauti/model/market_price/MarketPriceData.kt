package com.labs.sauti.model.market_price

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "market_prices")
data class MarketPriceData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @SerializedName("country")
    @Expose
    var country: String? = null,

    @SerializedName("market")
    @Expose
    var market: String? = null,

    @SerializedName("product_agg")
    @Expose
    @ColumnInfo(name = "product_agg")
    var productAgg: String? = null,

    @SerializedName("product_cat")
    @Expose
    @ColumnInfo(name = "product_cat")
    var productCat: String? = null,

    @SerializedName("product")
    @Expose
    var product: String? = null,

    @SerializedName("wholesale")
    @Expose
    var wholesale: Double? = null,

    @SerializedName("retail")
    @Expose
    var retail: Double? = null,

    @SerializedName("currency")
    @Expose
    var currency: String? = null,

    @SerializedName("date")
    @Expose
    var date: String? = null
)
