package com.jmlucero.alkewallet.data.room

import android.content.Context

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
    fun getCeuntaDao(): CuentaDAO = getDatabase().cuentaDAO()
}