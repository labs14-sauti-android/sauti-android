package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "regulated_good")
data class RegulatedGoodData(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,

    @SerializedName("country")
    @Expose
    val country: String,
    @SerializedName("language")
    @Expose
    val language: String
//    val prohibiteds: List<Prohibited>,
//    val restricteds: List<Restricted>,
//    val sensitives: List<Sensitive>
)

data class Prohibited(
    val name: String
)

data class Sensitive(
    val name: String
)

data class Restricted(
    val name: String
)