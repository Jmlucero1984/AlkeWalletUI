package com.jmlucero.alkewallet.mapper

import com.jmlucero.alkewallet.data.mapper.toUsuario
import com.jmlucero.alkewallet.data.room.dto.MonedaDTO
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

class UsuarioMonedaMapperTest(
) {
    @Test
    fun `should map UsuarioMonedaDTO to Usuario correctly`() {
        val monedaDTO = MonedaDTO(
            codigo = "CLP",
            nombre = "Peso Chileno",
            ratio_a_usd = 1.0
        )

        val usuarioMonedaDTO = UsuarioMonedaDTO(
            usuario_id = 1,
            nombre = "Jose",
            apellido = "Lucero",
            email = "joselucero@gmail.com",
            avatar_url = "https://example.com/avatar.jpg",
            balance = "1000.00",
            moneda =monedaDTO
        )

        val usuarioResult = usuarioMonedaDTO.toUsuario(true)
        Assert.assertArrayEquals("dfsdfsdf",
            arrayOf(
                usuarioResult.nombre,
                usuarioResult.apellido,
                usuarioResult.moneda_codigo),
            arrayOf(
                "Jose",
                "Lucero",
                "CLP"
            )
        )
    }
}

/*
    val usuario_id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    val avatar_url: String,
    val balance: String,
    val moneda: MonedaDTO
 */