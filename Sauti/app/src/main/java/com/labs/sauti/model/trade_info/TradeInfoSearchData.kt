package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trade_info_searches")
data class TradeInfoSearchData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null


    )