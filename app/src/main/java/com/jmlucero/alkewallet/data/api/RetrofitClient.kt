package com.jmlucero.alkewallet.data.api

import android.util.Log
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {

   // private const val BASE_URL = "http://10.0.2.2:8000/"

    private const val BASE_URL = "https://alkewallet.stehenmed.cl/"
    //En el servidor local hay que correr:
    // php -S 0.0.0.0:8000 -t public
   // private const val BASE_URL = "http://192.168.100.11:8000/"

    

    // Interceptor para añadir token automáticamente
    class AuthInterceptor @Inject constructor(
        private val sessionManager: SessionManager
    ) : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val token = sessionManager.getToken()

            val request = if (!token.isNullOrBlank()) {
                chain.request().newBuilder()
                    .header("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }

            return chain.proceed(request)
        }
    }
  //  private val authInterceptor = AuthInterceptor()
  @Provides
  @Singleton
  fun provideRetrofit(client: OkHttpClient): Retrofit {
      val gson = GsonBuilder()
          .setLenient()
          .create()

      return Retrofit.Builder()
          .baseUrl(BASE_URL)
          .client(client)
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build()
  }
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /*
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
*/


}