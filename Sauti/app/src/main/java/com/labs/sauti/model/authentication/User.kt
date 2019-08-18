package com.labs.sauti.model.authentication

data class User(
    var id: Long? = null,
    var username: String? = null,
    var phoneNumber: String? = null
)