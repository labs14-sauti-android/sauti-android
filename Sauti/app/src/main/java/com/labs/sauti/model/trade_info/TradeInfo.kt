package com.labs.sauti.model.trade_info

import androidx.room.Entity


//This will be the class that is stored into the UI. Will take in regulated goods as well.
@Entity(tableName = "trade_info_ui")
data class TradeInfo (
    var tradeinfoTopic : String,
    var tradeinfoTopicExpanded : String,
    var tradeinfoList : List<String>? = null,
    var tradeInfoProcedure: List<Procedure>? = null,
    var tradeInfoDocs : MutableList<RequiredDocument>? = null,
    var regulatedType : String? = null,
    val tradeInfoID : Long? = null
)
//TODO Markup with room annotations. Ideal way I would like data to be delivered in a list.
//TODO: Ignore will be removed for conversion.