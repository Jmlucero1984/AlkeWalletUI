package com.jmlucero.alkewallet.data.api

import okhttp3.internal.http2.ErrorCode

data class ApiError(
    val errorCode: ErrorCode,
    val mensaje: String
)
