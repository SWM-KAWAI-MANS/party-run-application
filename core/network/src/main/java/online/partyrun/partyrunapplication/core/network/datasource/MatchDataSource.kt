package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance

interface MatchDataSource {
    fun connectEvent(url: String, listener: EventSourceListener): EventSource

    suspend fun registerMatch(userSelectedMatchDistance: UserSelectedMatchDistance): ApiResult<MatchStatusResult>
    suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult>
    suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult>
}
