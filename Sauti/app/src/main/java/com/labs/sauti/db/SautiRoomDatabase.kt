package com.labs.sauti.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.labs.sauti.model.MarketPriceData
import com.labs.sauti.model.ProductRoom
import com.labs.sauti.model.RecentMarketPriceData
import com.labs.sauti.model.TradeInfoRoom



const val DATABASE_SCHEMA_VERSION = 1
const val DB_NAME = "local-db"


//Example of adding more entities

// entities = [ProductRoom::class, TradeInfo::Class]
@Database(
    entities = [
        ProductRoom::class,
        TradeInfoRoom::class,
        MarketPriceData::class,
        RecentMarketPriceData::class],
    version = DATABASE_SCHEMA_VERSION,
    exportSchema = false
)

abstract class SautiRoomDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao

    //add DAO
    abstract fun tradeInfoDao(): TradeInfoDao

    abstract fun marketPriceDao(): MarketPriceDao
    abstract fun recentMarketPriceDao(): RecentMarketPriceDao

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
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}