package com.labs.sauti.model.trade_info

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.sql.Date

//Model for Views


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
    val sensitives: List<Sensitive>,

    var timestamp: Long? = null
) {
    fun setTimestamp(){
        this.timestamp = System.currentTimeMillis()
    }

    fun getDateFromTimestamp() = timestamp?.let { Date(it) }
}



