package com.jmlucero.alkewallet.data.model

data class LoginResponse(
    val token: String,
    val type: String? = "Bearer"
)