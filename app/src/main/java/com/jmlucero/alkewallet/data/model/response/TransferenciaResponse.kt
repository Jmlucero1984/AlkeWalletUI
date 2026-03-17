package com.jmlucero.alkewallet.data.model.response

import java.math.BigDecimal

data class TransferenciaResponse(
    val mensaje :String,
    val nuevo_saldo: BigDecimal
)

