package com.labs.sauti.db

import com.labs.sauti.model.ProductRoom

//CAN BE REMOVED AND CONNECT DIRECTLY TO DAO

class ProductRepository(private val productDao: ProductDao) {


    //Executiion done on a seperate thread.
    // Observed LiveData will notify the observer when the data has changed.
    //List will be placed into LiveData in the ViewModel
    val allProducts: List<ProductRoom> = productDao.getAllProductsAlphabetized()

    //Must be called from non-ui thread.
    //TODO: Will do a check in the database and add accordingly.
    fun insertProduct(productRoom: ProductRoom) {
        productDao.insert(productRoom)
    }
}