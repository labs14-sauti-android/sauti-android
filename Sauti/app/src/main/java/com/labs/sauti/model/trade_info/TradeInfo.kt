package com.labs.sauti.model.trade_info

import androidx.room.Entity


//This will be the class that is stored into the UI. Will take in regulated goods as well.
@Entity(tableName = "trade_info_ui")
data class TradeInfo (
    var tradeinfoTopic : String,
    var tradeinfoTopicExpanded : String,
    var tradeinfoList : List<String>? = null,
    var tradeInfoProcedure: MutableList<Procedure>? = null,
    var tradeInfoCountry: String? = null,
    var tradeInfoDocs : MutableList<RequiredDocument>? = null,
    var tradeInfoAgencies: MutableList<BorderAgency>? = null,
    var regulatedType : String? = null,
    val tradeInfoID : Long? = System.currentTimeMillis()
)
//TODO Markup with room annotations. Ideal way I would like data to be delivered in a list.
//TODO: Ignore will be removed for conversion.