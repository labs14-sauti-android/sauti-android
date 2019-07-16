package com.labs.sauti.db

import androidx.lifecycle.LiveData
import com.labs.sauti.model.Product

class ProductRepository(private val productDao: ProductDao) {


    //Executiion done on a seperate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //List will be placed into LiveData in the ViewModel
    val allProducts: List<Product> = productDao.getAllProductssAlphabetized()

    //Called from non-ui thread.
    fun insertProduct(product: Product) {
        productDao.insert(product)
    }
}