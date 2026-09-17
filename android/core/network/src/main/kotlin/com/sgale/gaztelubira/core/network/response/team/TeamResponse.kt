package com.sgale.gaztelubira.core.network.response.team

import androidx.annotation.Keep
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TeamResponse(
    val id: FirebaseId = "",
    val name: String = "",
    val logo: String = ""
)
