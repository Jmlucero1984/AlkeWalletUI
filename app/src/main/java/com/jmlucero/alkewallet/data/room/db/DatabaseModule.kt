package com.jmlucero.alkewallet.data.room.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "wallet_db"
        )//.addMigrations(MIGRATION_1_2)
           // .addMigrations(MIGRATION_2_3)
            .build()
    }
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE usuarios ADD COLUMN balance TEXT NOT NULL DEFAULT '0.00'"

            )
            db.execSQL(
                "  CREATE TABLE cuentas (usuario_id INTEGER PRIMARY KEY NOT NULL,\n" +
                        "                balance TEXT NOT NULL DEFAULT '0.00',\n" +
                        "                updatedAt INTEGER NOT NULL DEFAULT 0)"

            )

        }
    }
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE usuarios ADD COLUMN moneda_codigo TEXT NOT NULL DEFAULT ''"

            )
            db.execSQL(
                "  CREATE TABLE monedas (codigo TEXT PRIMARY KEY NOT NULL,\n" +
                        "                nombre TEXT NOT NULL DEFAULT '',\n" +
                        "                ratio_a_usd DOUBLE NOT NULL DEFAULT 1.0)"

            )

        }
    }
    @Provides
    fun provideUsuarioDao(db: AppDatabase): UsuarioDAO {
        return db.usuarioDAO()
    }

    @Provides
    fun provideCuentaDao(db: AppDatabase): CuentaDAO {
        return db.cuentaDAO()
    }

    @Provides
    fun provideMonedaDao(db: AppDatabase): MonedaDAO {
        return db.monedaDAO()
    }
}