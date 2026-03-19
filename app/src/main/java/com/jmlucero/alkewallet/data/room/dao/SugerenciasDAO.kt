package com.jmlucero.alkewallet.data.room.dao

import androidx.room.Dao
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.SugerenciasTransfers
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface  SugerenciasDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSugerencia(sugerenciasTransfers: SugerenciasTransfers)

    @Query("SELECT u.* FROM usuarios u INNER JOIN SugerenciasTransfers s ON u.email = s.usuarioDestinoEmail WHERE s.usuarioPropietarioEmail = :usuarioPropietarioEmail AND u.email LIKE '%' || :query || '%' ORDER BY s.lastUsed DESC LIMIT 5")
    fun getSugerencias(usuarioPropietarioEmail: String, query: String): Flow<List<Usuario>>
}