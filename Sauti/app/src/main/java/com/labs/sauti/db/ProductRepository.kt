package com.labs.sauti.db

import com.labs.sauti.model.ProductData

//CAN BE REMOVED AND CONNECT DIRECTLY TO DAO
//or Will check locally then remotely with Rxjava call.

class ProductRepository(private val productDao: ProductDao) {


    //Executiion done on a seperate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //List will be placed into LiveData in the ViewModel
    val allProducts: List<ProductData> = productDao.getAllProductsAlphabetized()

    //Must be called from non-ui thread.
    //TODO: Will do a check in the database and add accordingly.
    fun insertProduct(productData: ProductData) {
        productDao.insert(productData)
    }
}