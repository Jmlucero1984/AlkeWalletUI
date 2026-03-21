package com.jmlucero.alkewallet.utils

import android.content.Context
import androidx.room.Room
import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.RetrofitClient
import com.jmlucero.alkewallet.data.api.SessionManager
import com.jmlucero.alkewallet.data.repository.ApiHandler
import com.jmlucero.alkewallet.data.repository.AuthRepository
import com.jmlucero.alkewallet.data.repository.UserRepository
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.SugerenciasDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO
import com.jmlucero.alkewallet.data.room.db.AppDatabase
import com.jmlucero.alkewallet.data.room.db.DatabaseModule
import com.jmlucero.alkewallet.di.AppModule
import com.jmlucero.alkewallet.viewmodel.AuthViewModel
import com.jmlucero.alkewallet.viewmodel.UserViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [
        AppModule::class,
        DatabaseModule::class,
        RetrofitClient::class
    ]
)
object TestAuthViewModelModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        // base de datos en memoria para tests, no persiste
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideSugerenciasDao(): SugerenciasDAO {
        return mockk(relaxed = true, relaxUnitFun = true)
    }



    // ✅ SessionManager (reemplaza AppModule)
    @Provides
    @Singleton
    fun provideSessionManager(): SessionManager {
        return mockk<SessionManager>(relaxed = true, relaxUnitFun = true)
    }

    // ✅ ApiService
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return mockk<ApiService>(relaxed = true, relaxUnitFun = true)
    }

    // ✅ Repositories
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return mockk<AuthRepository>(relaxed = true, relaxUnitFun = true)
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return mockk<UserRepository>(relaxed = true, relaxUnitFun = true)
    }

    // ✅ DAOs (reemplaza DatabaseModule)
    @Provides
    @Singleton
    fun provideUsuarioDao(): UsuarioDAO {
        return mockk<UsuarioDAO>(relaxed = true, relaxUnitFun = true)
    }

    @Provides
    @Singleton
    fun provideCuentaDao(): CuentaDAO {
        return mockk<CuentaDAO>(relaxed = true, relaxUnitFun = true)
    }

    @Provides
    @Singleton
    fun provideMonedaDao(): MonedaDAO {
        return mockk<MonedaDAO>(relaxed = true, relaxUnitFun = true)
    }
}