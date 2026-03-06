package com.jmlucero.alkewallet.data.room

import androidx.lifecycle.Lifecycle
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.UiState
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Usuario)

    @Query("SELECT * FROM usuarios WHERE isLoggedUser = true")
    fun getUsuario(): Flow<Usuario>

//    @Query("DELETE FROM users WHERE token = :token")
//    suspend fun deleteUserByToken(token: String)

//    @Query("SELECT * FROM users ORDER BY lastSync DESC LIMIT 1")
//    suspend fun getLastLoggedUser(): UserEntity?
}