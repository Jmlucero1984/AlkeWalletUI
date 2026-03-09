package com.jmlucero.alkewallet.data.model

data class SignUpNuevoUsuario(
    val nombre:String,
    val apellido:String,
    val email: String,
    val password:String,
    val moneda_codigo: String
)
