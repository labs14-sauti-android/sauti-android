package com.labs.sauti.db

import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.ProductRoom

@Dao
interface ProductDao : BaseDao<ProductRoom> {

    @Query(value = "SELECT * FROM products ORDER BY product_name")
    fun getAllProductsAlphabetized(): List<ProductRoom>

    @Query("DELETE FROM products")
    fun deleteAll()

}