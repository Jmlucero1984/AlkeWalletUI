package com.jmlucero.alkewallet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.Date

@Entity(tableName = "cuentas")
data class Cuenta(
    @PrimaryKey
    val usuario_id:Int,
    val balance: String,
    val updatedAt: Long
)
