package com.labs.sauti.model.trade_info


//This will be the class that is stored into the UI. Will take in regulated goods as well.
data class TradeInfo (
    var tradeinfoTopic : String,
    var tradeinfoTopicExpanded : String,
    var tradeinfoList : List<String>
)
//TODO Markup with room annotations. Ideal way I would like data to be delivered in a list.
//TODO: Ignore will be removed for conversion.