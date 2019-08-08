package com.labs.sauti.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.labs.sauti.model.*
import com.labs.sauti.model.market_price.MarketPriceData
import com.labs.sauti.model.market_price.MarketPriceSearchData
import com.labs.sauti.model.exchange_rate.ExchangeRateConversionData
import com.labs.sauti.model.exchange_rate.ExchangeRateData


const val DATABASE_SCHEMA_VERSION = 8
const val DB_NAME = "local-db"


//Example of adding more entities

// entities = [ProductRoom::class, TradeInfo::Class]
@Database(
    entities = [
        ProductData::class,
        TradeInfoData::class,
        MarketPriceData::class,
        MarketPriceSearchData::class,
        ExchangeRateData::class,
        ExchangeRateConversionData::class],
    version = DATABASE_SCHEMA_VERSION,
    exportSchema = false
)
abstract class SautiRoomDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao

    //add DAO
    abstract fun tradeInfoDao(): TradeInfoDao

    abstract fun marketPriceDao(): MarketPriceDao
    abstract fun marketPriceSearchDao(): MarketPriceSearchDao

    abstract fun exchangeRateDao(): ExchangeRateDao
    abstract fun exchangeRateConversionDao(): ExchangeRateConversionDao

    companion object {
        @Volatile
        private var INSTANCE: SautiRoomDatabase? = null

        fun getDatabase(context: Context) : SautiRoomDatabase{
            if (INSTANCE == null) {
                INSTANCE = createDatabase(context)
            }
            return INSTANCE!!
        }

        //Use Rxjava to create run this function
        private fun createDatabase(context: Context): SautiRoomDatabase {
            return Room.databaseBuilder(context, SautiRoomDatabase::class.java, DB_NAME)
                //TODO: To be removed once models are finalized and ready for final delivery.
                //NOTE: Not good practice since it destroys all the data previously.
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}