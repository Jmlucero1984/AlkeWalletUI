package com.jmlucero.alkewallet.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jmlucero.alkewallet.data.model.Moneda
import kotlinx.coroutines.flow.Flow
@Dao
interface MonedaDAO{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneda(moneda: Moneda)

    @Query("SELECT * FROM monedas WHERE codigo = :codigo")
    fun getMoneda(codigo:String): Flow<Moneda>


}