package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.ProductData
import io.reactivex.Completable

//TODO: Something like this will be implemented if we get to markeptplace


@Dao
interface ProductDao : BaseDao<ProductData> {

    @Query(value = "SELECT * FROM products ORDER BY product_name")
    fun getAllProductsAlphabetized(): List<ProductData>

    @Query("DELETE FROM products")
    fun deleteAll() : Completable

}