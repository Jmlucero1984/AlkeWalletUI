package com.jmlucero.alkewallet.data.model

import androidx.room.Entity


data class Usuario(
    val usuario_id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val avatar_url: String
)