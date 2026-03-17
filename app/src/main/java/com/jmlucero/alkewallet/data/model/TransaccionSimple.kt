package com.jmlucero.alkewallet.data.model

import java.math.BigDecimal

data class TransaccionSimple(
    val usuario_nombre:String,
    val usuario_avatar: String,
    val tipo_transaccion:String,
    val cantidad_efectiva: BigDecimal,
    val comentario: String,
    val fecha_creacion: DateTime
)
