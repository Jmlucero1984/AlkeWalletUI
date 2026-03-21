package com.jmlucero.alkewallet.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.model.entity.UsuarioConMoneda
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: Usuario)

    @Query("SELECT * FROM usuarios WHERE isLoggedUser = true")
    fun getUsuario(): Flow<Usuario?>

    @Query("UPDATE usuarios SET isLoggedUser = 0")
    suspend fun logoutAllUsers()

    @Query("UPDATE usuarios SET balance = :balance WHERE email = :email")
    suspend fun updateBalance(balance: String, email: String)


    @Query("UPDATE usuarios SET avatar_url = :avatarUrl WHERE isLoggedUser = 1")
    suspend fun updateAvatar(avatarUrl: String)
    @Transaction
    @Query("SELECT * FROM usuarios WHERE isLoggedUser = true")
    fun getUsuarioConMoneda(): Flow<UsuarioConMoneda?>
    @Transaction
    @Query("SELECT * FROM usuarios WHERE usuario_id = :id")
    suspend fun obtenerUsuarioConMoneda(id: Int): UsuarioConMoneda
//    @Query("DELETE FROM users WHERE token = :token")
//    suspend fun deleteUserByToken(token: String)

//    @Query("SELECT * FROM users ORDER BY lastSync DESC LIMIT 1")
//    suspend fun getLastLoggedUser(): UserEntity?
}