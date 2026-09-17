package com.sgale.gaztelubira.core.network.response.stats

import androidx.annotation.Keep
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class StatsResponse(
    val assists: List<FirebaseId> = emptyList(),
    val cleanSheets: List<FirebaseId> = emptyList(),
    val fails: List<FirebaseId> = emptyList(),
    val goals: List<FirebaseId> = emptyList(),
    val goalsProvoked: List<FirebaseId> = emptyList(),
    val penaltiesProvoked: List<FirebaseId> = emptyList(),
    val redCards: List<FirebaseId> = emptyList(),
    val saves: List<FirebaseId> = emptyList(),
    val yellowCards: List<FirebaseId> = emptyList()
)
