package com.sgale.gaztelubira.core.domain.auth

import com.sgale.gaztelubira.core.domain.model.user.UserModel

sealed interface AuthResult {
    data class Success(val user: UserModel): AuthResult
    data class Error(val message: String = "There was an error", val cause: Throwable? = null): AuthResult
}
