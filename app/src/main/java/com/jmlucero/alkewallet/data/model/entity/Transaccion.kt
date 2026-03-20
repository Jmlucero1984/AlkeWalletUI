package com.jmlucero.alkewallet.data.model.entity

import java.math.BigDecimal

data class Transaccion(
    val cuenta_origen_nombre:String,
    val usuario_origen_avatar: String,
    val cuenta_destino_nombre:String,
    val usuario_destino_avatar:String,
    val tipo_transaccion:String,
    val monto: BigDecimal,
    val cantidad_efectiva: BigDecimal,
    val comentario: String,
    val fecha_creacion: DateTime


//    val transaccion_id: Int,
//    val tipo_transaccion: String,
//    val cuenta_origen_id:Int,
//    val cuenta_destino_id:Int,
//    val cantidad: BigDecimal,
//    val moneda_origen_id: Int,
//    val moneda_destino_id: Int,
//    val ratio_a_dolar_origen: Double,
//    var ratio_a_dolar_destino: Double,
//    val factor_conversion: Double,
//    val cantidad_efectiva: BigDecimal,
//    val saldo_anterior: BigDecimal,
//    val saldo_poserior: BigDecimal,
//    val estado:String,
//    val fecha_creacion: OffsetDateTime

)