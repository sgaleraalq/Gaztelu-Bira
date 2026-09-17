package com.sgale.gaztelubira.core.network.firebase.response.user

import androidx.annotation.Keep
import com.sgale.gaztelubira.core.domain.model.utils.UserSessionId
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UserResponse(
    val id: UserSessionId = "",
    val name: String = "",
    val email: String = "",
    val img: String? = null,
    val role: String = ""
)
