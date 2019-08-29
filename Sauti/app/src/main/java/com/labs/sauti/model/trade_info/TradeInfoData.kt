package com.labs.sauti.model.trade_info

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.labs.sauti.helper.ProcedureConverter
import java.sql.Date
import java.util.*


//This will be the class that is stored into room

@Entity(tableName = "trade_info")
@TypeConverters(ProcedureConverter::class)
data class TradeInfoData(
    @PrimaryKey
    @ColumnInfo(name = "trade_info_id")
    var id: Long? = null,

    @SerializedName("dest")
    @Expose
    var dest: String? = null,

    @SerializedName("language")
    @Expose
    var language: String?= null,

    @SerializedName("origin")
    @Expose
    var origin: String? = null,



    @SerializedName("procedures")
    @Expose
    var procedures: MutableList<Procedure>? = null,

    @SerializedName("product")
    @Expose
    var product: String? = null,

    @SerializedName("productCat")
    @Expose
    var productCat: String? = null,

    @SerializedName("relevantAgencyData")
    @Expose
    var relevantAgencyData: MutableList<BorderAgency>? = null,

    @SerializedName("requiredDocumentData")
    @Expose
    var requiredDocumentData: MutableList<RequiredDocument>? = null,

    @SerializedName("taxes")
    @Expose
    var taxes: MutableList<Taxes>? = null,

    @SerializedName("value")
    @Expose
    var value: String?= null,

    //Regulated
    var regulatedCountry: String? = null,

    //Regulated Goods
    var prohibiteds: MutableList<Prohibited>? = null,

    //Regulated Goods
    var restricteds: MutableList<Restricted>? = null,

    //Regulated Goods
    var sensitives: MutableList<Sensitive>? = null,

    //Taxes
    var userCurrency: String? = null,

    //Taxes
    var destinationCurrency: String? = null,

    //Taxes
    var userToDestRate: Double? = null,

    var approximateValue: Long? = null

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





