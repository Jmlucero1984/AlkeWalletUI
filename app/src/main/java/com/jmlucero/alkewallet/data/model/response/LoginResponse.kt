package com.jmlucero.alkewallet.data.model.response

data class LoginResponse(
    val token: String,
    val type: String? = "Bearer"
)