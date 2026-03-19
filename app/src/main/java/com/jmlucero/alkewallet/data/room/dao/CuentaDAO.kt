package com.jmlucero.alkewallet.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.Cuenta
import kotlinx.coroutines.flow.Flow
@Dao
interface CuentaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuenta(cuenta: Cuenta)

    @Query("UPDATE cuentas SET balance = :balance WHERE usuarioEmail = :usuario_email")
    suspend fun updateBalance(balance: String, usuario_email: String)



    @Query("SELECT * FROM cuentas WHERE usuarioEmail= :usuario_email")
    fun getCuenta(usuario_email:String): Flow<Cuenta>
}



