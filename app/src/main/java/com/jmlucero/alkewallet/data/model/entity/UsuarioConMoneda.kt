package com.jmlucero.alkewallet.data.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class UsuarioConMoneda(
    @Embedded val usuario: Usuario,

    @Relation(
        parentColumn = "moneda_codigo",
        entityColumn = "codigo"
    )
    val moneda: Moneda
)