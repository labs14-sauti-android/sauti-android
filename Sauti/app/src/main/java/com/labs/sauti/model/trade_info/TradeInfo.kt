package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose


//This will be the class that is stored into the UI. Will take in regulated goods as well.
@Entity(tableName = "trade_info_favorites")
data class TradeInfo (
    var tradeinfoTopic : String,
    var tradeinfoTopicExpanded : String,
    var tradeinfoList : List<String>? = null,
    var tradeInfoProcedure: MutableList<Procedure>? = null,
    var tradeInfoCountry: String? = null,
    var tradeInfoDocs : MutableList<RequiredDocument>? = null,
    var tradeInfoAgencies: MutableList<BorderAgency>? = null,
    var regulatedType : String? = null,
    @Expose(serialize = false)
    var userId: Long? = null,
    @PrimaryKey
    val tradeInfoID : Long? = null,
    var isFavorite: Int? = null
)
//TODO Markup with room annotations. Ideal way I would like data to be delivered in a list.
//TODO: Ignore will be removed for conversion.