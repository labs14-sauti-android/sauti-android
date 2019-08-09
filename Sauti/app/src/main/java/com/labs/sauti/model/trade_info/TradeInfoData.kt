package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


//This will be the class that is stored into room

@Entity(tableName = "trade_info")
data class TradeInfoData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @SerializedName("dest")
    @Expose
    val dest: String,

    @SerializedName("language")
    @Expose
    val language: String,

    @SerializedName("origin")
    @Expose
    val origin: String,

    @SerializedName("product")
    @Expose
    val product: String,

    @SerializedName("productCat")
    @Expose
    val productCat: String,

    //TODO: Revisit models.
/*    @SerializedName("relevantAgencyData")
    @Expose
    val relevantAgencyData: List<RelevantAgencyData>,

    @SerializedName("requiredDocumentData")
    @Expose
    val requiredDocumentData: List<RequiredDocumentData>,

    @SerializedName("taxes")
    @Expose
    val taxes: List<TaxesData>,*/

    @SerializedName("value")
    @Expose
    val value: String
)





