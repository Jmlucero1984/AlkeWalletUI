package com.jmlucero.alkewallet.data.model


import java.math.BigDecimal

data class RetiroResponse(
    val mensaje :String,
    val nuevo_saldo: BigDecimal
)
