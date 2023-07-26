package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance

interface MatchRepository {

    /* REST */
    suspend fun registerMatch(userSelectedMatchDistance: UserSelectedMatchDistance): Flow<ApiResponse<MatchStatusResult>>
    suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest): Flow<ApiResponse<MatchStatusResult>>
    suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest): Flow<ApiResponse<MatchStatusResult>>

    /* SSE */
    fun connectWaitingEventSource(listener: EventSourceListener): EventSource
    fun connectMatchResultEventSource(listener: EventSourceListener): EventSource
}
