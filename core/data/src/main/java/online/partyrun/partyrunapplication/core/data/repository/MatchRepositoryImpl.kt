package online.partyrun.partyrunapplication.core.data.repository

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import javax.inject.Inject


class MatchRepositoryImpl @Inject constructor(
    private val dataSource: MatchDataSource
) : MatchRepository {
    /* REST */
    override suspend fun registerMatch(userSelectedMatchDistance: UserSelectedMatchDistance) = apiRequestFlow {
        dataSource.registerMatch(userSelectedMatchDistance)
    }

    override suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest) = apiRequestFlow {
        dataSource.acceptMatch(matchDecisionRequest)
    }

    override suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest) = apiRequestFlow {
        dataSource.declineMatch(matchDecisionRequest)
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
