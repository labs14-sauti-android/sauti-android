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

    var approximateValue: Double? = null

) {
    fun convertCountryCodetoCountry(code: String): String {
        when(code) {
            "KEN" -> (return "Kenya")
            "BDI"-> (return "Burundi")
            "DRC"-> (return "Democratic Republic of the Congo")
            "MWI"-> (return "Malawi")
            "RWA"-> (return "Rwanda")
            "TZA"-> (return "Tanzania")
            "UGA"-> (return "Uganda")
            else -> return ""
        }
    }
}

fun TradeInfoData.toTradeInfoTaxes(): TradeInfoTaxes {
    val taxTI = TradeInfoTaxes(
        taxProduct = (product as String),
        currentCurrency = (userCurrency as String),
        endCurrency =  (destinationCurrency as String),
        taxList =  (taxes as MutableList<Taxes>),
        rate = (userToDestRate as Double),
        initialAmount = (approximateValue as Double))

    taxTI.getTaxesConversions()

    return taxTI
}



//TODO: Must break down
fun TradeInfoData.toTradeInfo() =
    if (relevantAgencyData != null) TradeInfo(
        tradeinfoTopic = "Border Agencies",
        tradeinfoTopicExpanded = "Push to View More Information About The Agency",
        tradeInfoAgencies = relevantAgencyData as MutableList<BorderAgency>,
        tradeInfoID = id)
    else if (requiredDocumentData != null) TradeInfo(tradeinfoTopic = "Required Documents",
        tradeinfoTopicExpanded = "Push to View More Information About The Document",
        tradeInfoDocs = requiredDocumentData as MutableList<RequiredDocument>,
        tradeInfoID = id)
    else if (procedures != null) {
        val destChoice = convertCountryCodetoCountry(this.dest!!)
        TradeInfo("Border Procedures",
            """To $destChoice""",
            tradeInfoProcedure = procedures as MutableList<Procedure>,
            tradeInfoID = id)
    }
    else if (requiredDocumentData != null) TradeInfo(tradeinfoTopic = "Required Documents",
        tradeinfoTopicExpanded = "Push to View More Information About The Document",
        tradeInfoDocs = requiredDocumentData as MutableList<RequiredDocument>,
        tradeInfoID = id
    )
    else if (prohibiteds != null) {
        val list = mutableListOf<String>()
        prohibiteds?.forEach { pro ->
            list.add(pro.name)
        }
        TradeInfo("Regulated Goods",
            "These commodities are prohibited",
            list,
            regulatedType = "Prohibited Goods",
            tradeInfoID = id)
    }
    else if (sensitives != null) {
        val list = mutableListOf<String>()
        sensitives?.forEach { sensitive ->
            list.add(sensitive.name)
        }

        TradeInfo("Regulated Goods",
            "These commodities are sensitive",
            list,
            regulatedType = "Sensitive Goods",
            tradeInfoID = id)
    }
    else if (restricteds != null) {
        val list = mutableListOf<String>()
        restricteds?.forEach { rest ->
            list.add(rest.name)
        }

        TradeInfo("Regulated Goods",
            "These commodities are restricted",
            list,
            regulatedType = "Restricted Goods",
            tradeInfoID = id)
    }
    else TradeInfo("Nothing", "Nothing")



