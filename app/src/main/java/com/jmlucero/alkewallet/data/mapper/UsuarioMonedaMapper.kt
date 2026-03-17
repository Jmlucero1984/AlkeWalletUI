package com.jmlucero.alkewallet.data.mapper

import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.Usuario
import com.jmlucero.alkewallet.data.room.dto.MonedaDTO
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO


    fun UsuarioMonedaDTO.toUsuario(owner: Boolean): Usuario {
        return Usuario(
            usuario_id = usuario_id,
            nombre = nombre,
            apellido = apellido,
            email = email,
            avatar_url = avatar_url,
            balance = balance ?: "0.00",
            moneda_codigo = moneda.codigo,
            isLoggedUser = owner
        )
    }
    fun MonedaDTO.toMoneda(): Moneda {
        return Moneda(
            codigo = codigo,
            nombre = nombre,
            ratio_a_usd = ratio_a_usd
        )
    }
