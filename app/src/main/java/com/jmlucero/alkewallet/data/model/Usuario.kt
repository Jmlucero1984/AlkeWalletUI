package com.jmlucero.alkewallet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey
    val usuario_id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val avatar_url: String,
    var isLoggedUser: Boolean = false,
    var balance: String="0.00"
)