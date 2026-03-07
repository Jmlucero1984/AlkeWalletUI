package com.jmlucero.alkewallet.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.Usuario
import kotlinx.coroutines.flow.Flow
@Dao
interface MonedaDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneda(moneda: Moneda)

    @Query("SELECT * FROM monedas WHERE codigo = :codigo")
    fun getMoneda(codigo:String): Flow<Moneda>


}