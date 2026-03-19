package com.jmlucero.alkewallet.data.model

import androidx.room.Entity

@Entity(
    tableName = "SugerenciasTransfers",
    primaryKeys = ["usuarioPropietarioEmail", "usuarioDestinoEmail"]
)
data class SugerenciasTransfers(
    val usuarioPropietarioEmail: String,
    val usuarioDestinoEmail: String,
    val lastUsed: Long

)
