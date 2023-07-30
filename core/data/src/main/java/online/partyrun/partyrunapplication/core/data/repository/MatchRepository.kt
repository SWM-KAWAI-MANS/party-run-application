package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.match.MatchDecision
import online.partyrun.partyrunapplication.core.model.match.MatchStatus
import online.partyrun.partyrunapplication.core.model.match.RunningDistance

interface MatchRepository {

    /* REST */
    suspend fun registerMatch(runningDistance: RunningDistance): Flow<ApiResponse<MatchStatus>>
    suspend fun acceptMatch(matchDecision: MatchDecision): Flow<ApiResponse<MatchStatus>>
    suspend fun declineMatch(matchDecision: MatchDecision): Flow<ApiResponse<MatchStatus>>

    /* SSE */
    fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit): EventSourceListener
    fun createWaitingEventSource(listener: EventSourceListener): EventSource
    fun createMatchResultEventSource(listener: EventSourceListener): EventSource

    fun connectWaitingEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun disconnectWaitingEventSource()
    fun disconnectMatchResultEventSource()
}
