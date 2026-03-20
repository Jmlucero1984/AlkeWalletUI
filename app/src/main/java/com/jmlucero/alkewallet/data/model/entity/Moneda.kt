package com.jmlucero.alkewallet.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monedas")
data class Moneda(
    @PrimaryKey
    val codigo: String,   // CLP, USD, ARS
    val nombre: String,
    val ratio_a_usd: Double
)

