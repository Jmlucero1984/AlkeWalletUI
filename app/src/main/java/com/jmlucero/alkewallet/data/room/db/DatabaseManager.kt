package com.jmlucero.alkewallet.data.room.db

import android.content.Context
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.SugerenciasDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO

// 1. Database Manager
object DatabaseManager {

    private var instance: AppDatabase? = null

    fun init(context: Context) {
        if (instance == null) {
            instance = AppDatabase.getInstance(context.applicationContext)
        }
    }

    fun getDatabase(): AppDatabase {
        return instance ?: throw IllegalStateException("DatabaseManager must be initialized")
    }

    fun getUserDao(): UsuarioDAO = getDatabase().usuarioDAO()
    fun getCuentaDao(): CuentaDAO = getDatabase().cuentaDAO()
    fun getMonedaDao(): MonedaDAO = getDatabase().monedaDAO()
    fun getSugerenciasDao(): SugerenciasDAO = getDatabase().sugerenciasDAO()

}