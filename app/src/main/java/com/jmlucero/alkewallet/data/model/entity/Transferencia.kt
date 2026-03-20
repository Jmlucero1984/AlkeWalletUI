package com.jmlucero.alkewallet.data.model.entity

data class Transferencia(
    val emailUsuarioDestino:String,
    val montoTransferencia:String,
    val tipoTransferencia:String,
    val comentario:String

)
