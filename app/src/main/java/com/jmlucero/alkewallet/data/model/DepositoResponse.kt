package com.jmlucero.alkewallet.data.model

import java.math.BigDecimal

data class DepositoResponse(
    val mensaje :String,
    val nuevo_saldo: BigDecimal
)

