package com.sgale.gaztelubira.core.domain.model.player

import androidx.compose.runtime.Immutable
import com.sgale.gaztelubira.core.domain.model.player.Player.Companion.ERROR_PLAYER
import com.sgale.gaztelubira.core.domain.model.stats.Stats
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId

@Immutable
data class PlayerStats(
    val id: FirebaseId,
    val player: Player,
    val stats: Map<FirebaseId, Stats>, // Map match to stats
    val percentage: Double = 0.0
) {
    companion object {
        val ERROR_PLAYER_STATS = PlayerStats(
            id = "",
            player = ERROR_PLAYER,
            stats = emptyMap()
        )
    }
}
