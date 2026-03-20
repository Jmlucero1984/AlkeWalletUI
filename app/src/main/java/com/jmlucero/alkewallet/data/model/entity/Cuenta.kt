package com.jmlucero.alkewallet.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cuentas")
data class Cuenta(
    @PrimaryKey
    val usuarioEmail:String,
    val balance: String,
    val updatedAt: Long
)
