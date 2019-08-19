package com.labs.sauti.view_state.authentication

import com.labs.sauti.model.authentication.User

class SignedInUserViewState(
    var isLoading: Boolean = false,
    var user: User? = null
)