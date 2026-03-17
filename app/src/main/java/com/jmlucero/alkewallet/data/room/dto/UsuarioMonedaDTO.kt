package com.jmlucero.alkewallet.data.room.dto

import com.jmlucero.alkewallet.data.room.dto.MonedaDTO

data class UsuarioMonedaDTO(
    val usuario_id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val avatar_url: String,
    val balance: String,
    val moneda: MonedaDTO

)