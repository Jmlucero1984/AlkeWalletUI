package com.jmlucero.alkewallet.data.room.db
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.jmlucero.alkewallet.data.model.entity.Cuenta
import com.jmlucero.alkewallet.data.model.entity.Moneda
import com.jmlucero.alkewallet.data.model.entity.SugerenciasTransfers
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.SugerenciasDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO

@Database(
    entities = [Usuario::class, Cuenta::class, Moneda::class, SugerenciasTransfers::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun cuentaDAO(): CuentaDAO

    abstract fun monedaDAO(): MonedaDAO
    abstract fun sugerenciasDAO(): SugerenciasDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wallet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}