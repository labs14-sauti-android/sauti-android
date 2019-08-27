package com.labs.sauti.model.trade_info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date
import java.util.*


//This will be the class that is stored into room

@Entity(tableName = "trade_info")
data class TradeInfoData(
    @PrimaryKey
    @ColumnInfo(name = "trade_info_id")
    var id: Long? = null,

    @SerializedName("dest")
    @Expose
    val dest: String,

    @SerializedName("language")
    @Expose
    val language: String,

    @SerializedName("origin")
    @Expose
    val origin: String? = null,

    @SerializedName("procedures")
    @Expose
    val procedures: List<Procedure>? = null,

    @SerializedName("product")
    @Expose
    val product: String? = null,

    @SerializedName("productCat")
    @Expose
    val productCat: String? = null,

    @SerializedName("relevantAgencyData")
    @Expose
    var relevantAgencyData: List<BorderAgency>? = null,

    @SerializedName("requiredDocumentData")
    @Expose
    val requiredDocumentData: List<RequiredDocument>? = null,

    @SerializedName("taxes")
    @Expose
    val taxes: List<Taxes>? = null,

    @SerializedName("value")
    @Expose
    val value: String,

    //Regulated Goods
    val prohibiteds: List<Prohibited>? = null,

    //Regulated Goods
    val restricteds: List<Restricted>? = null,

    //Regulated Goods
    val sensitives: List<Sensitive>? = null,

    //Taxes
    val userCurrency: String? = null,

    //Taxes
    val destinationCurrency: String? = null,

    //Taxes
    val userToDestRate: Double? = null,

    val approximateValue: Long? = null

)

/*{
    fun setTimestamp(){
        this.timestamp = System.currentTimeMillis()
    }

    fun getDateFromTimestamp() = timestamp?.let { Date(it) }
}*/

fun TradeInfoData.toBorderProcedures(procedure: List<Procedure>) {


}

fun TradeInfoData.toRequiredDocuments(){

}

fun TradeInfoData.toRelatedGoods() {

}

/*fun TradeInfoData.toTaxes(): TradeInfoTaxes {
    return TradeInfoTaxes(
        taxProduct = product
        currentCurrency = currency
    )
}*/





