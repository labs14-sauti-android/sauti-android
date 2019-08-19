package com.labs.sauti.model.authentication

data class User(
    var id: Long? = null,
    var username: String? = null,
    var phoneNumber: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var location: String? = null,
    var gender: String? = null
)