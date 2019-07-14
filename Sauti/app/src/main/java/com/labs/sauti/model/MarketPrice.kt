package com.labs.sauti.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MarketPrice {

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("market")
    @Expose
    var market: String? = null

    @SerializedName("product_agg")
    @Expose
    var productAgg: String? = null

    @SerializedName("product_cat")
    @Expose
    var productCat: String? = null

    @SerializedName("product")
    @Expose
    var product: String? = null

    @SerializedName("wholesale")
    @Expose
    var wholesale: Long? = null

    @SerializedName("retail")
    @Expose
    var retail: Long? = null

    @SerializedName("currency")
    @Expose
    var currency: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

}