package com.labs.sauti.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.labs.sauti.model.Product

@Dao
interface ProductDao : BaseDao<Product> {

    @Query(value = "SELECT * FROM products ORDER BY product_name")
    fun getAllProductssAlphabetized(): List<Product>

}