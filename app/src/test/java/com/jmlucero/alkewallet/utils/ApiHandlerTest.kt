package com.jmlucero.alkewallet.utils

import android.util.Log
import com.jmlucero.alkewallet.data.model.entity.UiState
import com.jmlucero.alkewallet.data.repository.ApiHandler
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response

class ApiHandlerTest {
    private val apiHandler = ApiHandler()

    @Test
    fun `safeApiCall should emit loading then error`() = runTest {
        val results = apiHandler.safeApiCall {
            Response.error<String>(
                400,
                ResponseBody.create(null, "Bad Request")
            )
        }.toList()

        assertTrue(results[0] is UiState.Loading)
        assertTrue(results[1] is UiState.Error)
        assertEquals(
            "Error 400: Bad Request",
            (results[1] as UiState.Error).message
        )
    }

    @Test
    fun `safeApiCall should return error when exception Network error is thrown`() = runTest {
        val results = apiHandler.safeApiCall<String> {
            throw RuntimeException("Network error")
        }.toList()

        assertTrue(results[0] is UiState.Loading)
        assertTrue(results[1] is UiState.Error)
        assertEquals(
            "Error de red",
            (results[1] as UiState.Error).message
        )
    }
    @Test
    fun `safeApiCall should return error when exception Timeout is thrown`() = runTest {
        val results = apiHandler.safeApiCall<String> {
            throw RuntimeException("Timeout")
        }.toList()

        assertTrue(results[0] is UiState.Loading)
        assertTrue(results[1] is UiState.Error)
        assertEquals(
            "Tiempo de espera agotado",
            (results[1] as UiState.Error).message
        )
    }

}