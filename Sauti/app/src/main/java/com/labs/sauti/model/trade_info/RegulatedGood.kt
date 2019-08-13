package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "regulated_good")
data class RegulatedGoodData(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    @SerializedName("country")
    @Expose
    val country: String,
    @SerializedName("language")
    @Expose
    val language: String,

    @SerializedName("prohibiteds")
    @Expose
    val prohibiteds: List<Prohibited>,

    @SerializedName("restricteds")
    @Expose
    val restricteds: List<Restricted>,

    @SerializedName("sensitives")
    @Expose
    val sensitives: List<Sensitive>
)

@Entity(tableName = "prohibited_goods")
data class Prohibited(

    @SerializedName("name")
    @Expose
    val name: String
)

@Entity(tableName = "sensitive_goods")
data class Sensitive(

    @SerializedName("name")
    @Expose
    val name: String
)

@Entity(tableName = "restricted_goods")
data class Restricted(

    @SerializedName("names")
    @Expose
    val name: String
)