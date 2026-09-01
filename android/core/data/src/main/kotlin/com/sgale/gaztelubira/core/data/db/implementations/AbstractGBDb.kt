package com.sgale.gaztelubira.core.data.db.implementations

import com.sgale.gaztelubira.core.data.db.GBDatabase
import com.sgale.gaztelubira.core.data.db.dao.BaseDao
import com.sgale.gaztelubira.core.data.mappers.asMatchModel
import com.sgale.gaztelubira.core.data.mappers.asPlayerDomain
import com.sgale.gaztelubira.core.data.mappers.asTeamModel
import com.sgale.gaztelubira.core.domain.model.match.MatchModel
import com.sgale.gaztelubira.core.domain.model.player.PlayerModel
import com.sgale.gaztelubira.core.domain.model.team.TeamModel
import com.sgale.gaztelubira.core.domain.model.utils.FirebaseId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

abstract class AbstractGBDb(
    protected val db: GBDatabase
) {
    private val matchesDao = db.getMatchesDao()
    private val playersDao = db.getPlayersDao()
    private val teamsDao = db.getTeamsDao()

    suspend fun getMatchesMap(): Map<FirebaseId, MatchModel> =
        matchesDao.getMatches().map { it.asMatchModel(getTeamsMap()) }.associateBy { it.id }
    suspend fun getPlayersMap(): Map<FirebaseId, PlayerModel> =
        playersDao.getPlayers().map { it.asPlayerDomain() }.associateBy { it.id }
    suspend fun getTeamsMap(): Map<FirebaseId, TeamModel> =
        teamsDao.getTeams().map { it.asTeamModel() }.associateBy { it.id }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected fun <Entity, Model, K : Comparable<K>> getFlow(
        source: Flow<List<Entity>>,
        mapper: suspend (Entity) -> Model,
        keySelector: (Model) -> K
    ): Flow<List<Model>> =
        source.mapLatest { list ->
            val mapped = ArrayList<Model>(list.size)
            list.forEach { element -> mapped += mapper(element) }
            mapped.sortedBy(keySelector)
        }

    protected suspend fun <T, E> insertList(
        items: List<T>,
        mapper: (T) -> E,
        dao: BaseDao<E>
    ) {
        runCatching {
            items.forEach { item -> dao.insert(mapper(item)) }
        }.onFailure { e ->
            println("GBDatabase error inserting ${e.message}")
            throw e
        }
    }
}