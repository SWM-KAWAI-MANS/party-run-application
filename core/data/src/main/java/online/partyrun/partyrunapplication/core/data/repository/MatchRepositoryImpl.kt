package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.data.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import javax.inject.Inject


class MatchRepositoryImpl @Inject constructor(
    private val dataSource: MatchDataSource
) : MatchRepository {
    /* REST */
    override suspend fun registerToBattleMatchingQueue(userSelectedMatchDistance: UserSelectedMatchDistance) = apiRequestFlow {
        dataSource.registerToBattleMatchingQueue(userSelectedMatchDistance)
    }

    override suspend fun sendAcceptBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest) = apiRequestFlow {
        dataSource.acceptBattleMatchingQueue(matchDecisionRequest)
    }

    override suspend fun sendDeclineBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest) = apiRequestFlow {
        dataSource.declineBattleMatchingQueue(matchDecisionRequest)
    }

    /* SSE */
    override fun connectWaitingEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/waiting/event")
        return dataSource.connectEvent(url = url, listener = listener)
    }

    override fun connectMatchResultEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/match/event")
        return dataSource.connectEvent(url = url, listener = listener)
    }
}
