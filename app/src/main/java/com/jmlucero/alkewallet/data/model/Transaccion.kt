package com.jmlucero.alkewallet.data.model

import java.math.BigDecimal
import java.sql.Date
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class Transaccion(
    val cuenta_origen_nombre:String,
    val cuenta_destino_nombre:String,
    val tipo_transaccion:String,
    val monto: BigDecimal,
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