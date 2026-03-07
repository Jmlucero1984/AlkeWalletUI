package com.jmlucero.alkewallet.data.room
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import android.service.chooser.AdditionalContentContract
import com.jmlucero.alkewallet.data.model.Cuenta
import com.jmlucero.alkewallet.data.model.Moneda
import com.jmlucero.alkewallet.data.model.Usuario

@Database(
    entities = [Usuario::class, Cuenta::class, Moneda::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDAO(): UsuarioDAO
    abstract fun cuentaDAO(): CuentaDAO

    abstract fun monedaDAO(): MonedaDAO
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