package com.sgale.gaztelubira.core.domain.model.player

import androidx.compose.runtime.Stable
import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

@Stable
data class PlayerStats(
    val id: FirebaseId,
    val player: Player,
    val stats: Map<FirebaseId, Stats>, // Map match to stats
    val percentage: Double = 0.0
)
