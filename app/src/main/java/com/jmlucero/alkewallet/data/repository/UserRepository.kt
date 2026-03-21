package com.jmlucero.alkewallet.data.repository

import android.util.Log
import com.jmlucero.alkewallet.data.api.ApiService
import com.jmlucero.alkewallet.data.api.SessionManager
import com.jmlucero.alkewallet.data.mapper.toMoneda
import com.jmlucero.alkewallet.data.mapper.toUsuario
import com.jmlucero.alkewallet.data.model.response.AvatarResponse
import com.jmlucero.alkewallet.data.model.entity.Balance
import com.jmlucero.alkewallet.data.model.entity.Cuenta
import com.jmlucero.alkewallet.data.model.entity.Deposito
import com.jmlucero.alkewallet.data.model.response.DepositoResponse
import com.jmlucero.alkewallet.data.model.entity.Retiro
import com.jmlucero.alkewallet.data.model.response.RetiroResponse
import com.jmlucero.alkewallet.data.model.entity.SignUpNuevoUsuario
import com.jmlucero.alkewallet.data.model.entity.SugerenciasTransfers
import com.jmlucero.alkewallet.data.model.response.SignUpResponse
import com.jmlucero.alkewallet.data.model.entity.Transferencia
import com.jmlucero.alkewallet.data.model.response.TransferenciaResponse

import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.model.entity.UsuarioConMoneda
import com.jmlucero.alkewallet.data.room.dao.CuentaDAO
import com.jmlucero.alkewallet.data.room.dao.MonedaDAO
import com.jmlucero.alkewallet.data.room.dao.SugerenciasDAO
import com.jmlucero.alkewallet.data.room.dao.UsuarioDAO
import com.jmlucero.alkewallet.data.room.dto.UsuarioMonedaDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import javax.inject.Inject
import kotlin.String

class UserRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager,
    private val usuarioDAO: UsuarioDAO,
    private val cuentaDAO: CuentaDAO,
    private val monedaDAO: MonedaDAO,
    private val sugerenciasDAO: SugerenciasDAO,
    private val apiHandler: ApiHandler
) {



    suspend fun insertLoggedUsuario(usuario: Usuario) {
        usuarioDAO.insertUser(usuario)
    }
    
    suspend fun getSugerencias(_query: String): Flow<List<Usuario>> {
        val email = sessionManager.getEmail() ?: return flowOf(emptyList())
        return sugerenciasDAO.getSugerencias(
            email,
            query =_query
        )
    }
    
    
    suspend fun updateBalance(balance: String, usuarioEmail:String) {
        cuentaDAO.updateBalance(balance,usuarioEmail)
        usuarioDAO.updateBalance(balance,usuarioEmail)

    }


    suspend fun updateUsuarioLocalAvatar(url:String) {
        usuarioDAO.updateAvatar(url)
    }
    fun getUsuarioLocal(): Flow<Usuario?> {
        return  usuarioDAO.getUsuario()
    }
    fun getUsuarioConMonedaLocal(): Flow<UsuarioConMoneda?> {
        return  usuarioDAO.getUsuarioConMoneda()
    }
    fun getCuentaLocal(usuario_email:String):Flow<Cuenta?> {
        return cuentaDAO.getCuenta(usuario_email)
    }




    suspend fun getUsuarioPorEmail(email: String): Flow<UiState<UsuarioMonedaDTO>> =
        apiHandler.safeApiCall  {
            apiService.getUsuarioPorEmail(email)
        }.onEach { state ->
            if (state is UiState.Success) {
                val usuario = state.data.toUsuario(false)
                val moneda = state.data.moneda.toMoneda()
                usuarioDAO.insertUser(usuario)
                monedaDAO.insertMoneda(moneda)
                sugerenciasDAO.insertSugerencia(
                    SugerenciasTransfers(
                usuarioPropietarioEmail = sessionManager.getEmail()!!,
                usuarioDestinoEmail = usuario.email,
                        lastUsed = System.currentTimeMillis()
                    )
                )
            }
        }


     fun getBalance(): Flow<UiState<Balance>> =
        apiHandler.safeApiCall  {
            apiService.getBalance()
        }

     fun doRetiro(retiro: Retiro): Flow<UiState<RetiroResponse>> =
        apiHandler.safeApiCall  {
            apiService.retiro(retiro)
        }
     fun doDeposito(deposito: Deposito): Flow<UiState<DepositoResponse>> =
        apiHandler.safeApiCall  {
            apiService.deposito(deposito)
        }

     fun uploadAvatar(avatar: MultipartBody.Part):Flow<UiState<AvatarResponse>> =
        apiHandler.safeApiCall  {
            apiService.uploadAvatar(avatar)
        }

     fun doTransferencia(transferencia: Transferencia): Flow<UiState<TransferenciaResponse>> =
        apiHandler.safeApiCall  {
            apiService.transferencia(transferencia)
        }
     fun getUsuarios(): Flow<UiState<List<Usuario>>> =
        apiHandler.safeApiCall  {
            apiService.getUsuarios()
        }
     fun signUpUsuario(signUpNuevoUsuario: SignUpNuevoUsuario): Flow<UiState<SignUpResponse>> =
        apiHandler.safeApiCall  {
            apiService.signUpUsuario(signUpNuevoUsuario)
        }




    suspend fun logout() {
        // 1. Limpiar usuario logueado
        usuarioDAO.logoutAllUsers()

        // 2. Borrar token (MUY importante)
        sessionManager.clearToken()

        // 3. (Opcional pero recomendado) limpiar otras tablas cacheadas
        //transactionDao.clearAll()
        //contactsDao.clearAll()
    }

     fun getCuentaUsuarioLogueado(): Flow<Cuenta?> {
         val email = sessionManager.getEmail()
         Log.i("USER REPOSITORY", "EMAIL LOGUEADO $email")
         val let = email?.let {
             val _cuenta: Flow<Cuenta> = cuentaDAO.getCuenta(it)
             _cuenta.let { flow ->
                 Log.i("USER REPOSITORY", "EMAIL LOGUEADO $_cuenta")
                 return _cuenta
             }

         }
         return flowOf(null)

     }

    }



        //return sessionManager.getEmail()?.let { cuentaDAO.getCuenta(it) }!!


