package com.jmlucero.alkewallet.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.String

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
        ).addMigrations(MIGRATION_1_2)
            .build()
    }
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {

            db.execSQL(
                "ALTER TABLE usuarios ADD COLUMN balance TEXT NOT NULL DEFAULT '0.00'"

            )
            db.execSQL(
                "  CREATE TABLE cuentas (usuario_id INTEGER PRIMARY KEY NOT NULL,\n" +
                        "                balance TEXT NOT NULL DEFAULT 0.00,\n" +
                        "                updatedAt INTEGER NOT NULL DEFAULT 0)"

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
}