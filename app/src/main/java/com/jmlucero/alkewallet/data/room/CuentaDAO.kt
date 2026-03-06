package com.jmlucero.alkewallet.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow
@Dao
interface CuentaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCuenta(cuenta: Cuenta)

    @Query("SELECT * FROM cuentas")
    fun getCuenta(): Flow<Cuenta>
}



