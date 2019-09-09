package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.trade_info.RegulatedGoodData
import io.reactivex.Single

@Dao
interface RegulatedGoodDao : BaseDao<RegulatedGoodData> {

    @Query("SELECT DISTINCT country from regulated_good where language=:language")
    fun getRegulatedGoodsCountries(language : String) : Single<MutableList<String>>

}