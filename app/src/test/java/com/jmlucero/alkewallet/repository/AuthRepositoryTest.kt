package com.jmlucero.alkewallet.repository

import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.model.entity.Usuario
import com.jmlucero.alkewallet.data.model.response.LoginResponse
import com.jmlucero.alkewallet.data.repository.AuthRepository
import com.jmlucero.alkewallet.viewmodel.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {
    private val repository = mockk<AuthRepository>()
    private lateinit var authViewModel: AuthViewModel

    // 1. Crea el dispatcher de prueba
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        // 2. Reemplaza Dispatchers.Main con el dispatcher de prueba
        Dispatchers.setMain(testDispatcher)
        authViewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        // 3. Restaura Dispatchers.Main al terminar
        Dispatchers.resetMain()
    }

    @Test
    @DisplayName("login debe emitir estado Loading primero")
    fun `login debe emitir estado Loading primero`() = runTest {

        val loginResponse = LoginResponse(token = "test_token")
        coEvery { repository.login(any(), any()) } returns flow {
            emit(UiState.Loading)
            emit(UiState.Success(loginResponse))
        }

        // 1. Arranca el colector en background ANTES de llamar login
        val states = mutableListOf<UiState<LoginResponse>>()
        val job = launch(UnconfinedTestDispatcher()) {
            authViewModel.loginState.collect {
                println(">>> Estado recibido: $it") // 👈 así de simple
                states.add(it) }
        }

        // 2. Ahora dispara el login
        authViewModel.login("test", "1234")
        advanceUntilIdle() // 3. Espera que todas las corrutinas terminen

        // 4. Assertions
        val successState = states[1] as? UiState.Success

        assertTrue(
            "Se esperaba UiState.Success como segundo estado, pero se recibió: ${states.getOrNull(1)}",
            states[1] is UiState.Success
        )

        job.cancel() // 5. Limpia el colector
    }

    @Test
    @DisplayName("login debe emitir estado Success con token correcto")
    fun `login debe emitir estado Success con token correcto`() = runTest {

        val loginResponse = LoginResponse(token = "test_token")
        coEvery { repository.login(any(), any()) } returns flow {
            emit(UiState.Loading)
            emit(UiState.Success(loginResponse))
        }

        // 1. Arranca el colector en background ANTES de llamar login
        val states = mutableListOf<UiState<LoginResponse>>()
        val job = launch(UnconfinedTestDispatcher()) {
            authViewModel.loginState.collect {
                println(">>> Estado recibido: $it") // 👈 así de simple
                states.add(it) }
        }

        // 2. Ahora dispara el login
        authViewModel.login("test", "1234")
        advanceUntilIdle() // 3. Espera que todas las corrutinas terminen

        // 4. Assertions
        val successState = states[1] as? UiState.Success

        assertEquals(
            "El token recibido no coincide con el esperado",
            "test_token",
            successState?.data?.token
        )


        job.cancel() // 5. Limpia el colector
    }
}