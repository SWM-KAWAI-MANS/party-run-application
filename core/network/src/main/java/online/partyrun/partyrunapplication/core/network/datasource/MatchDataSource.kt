package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.CancelMatchResponse
import online.partyrun.partyrunapplication.core.network.model.response.MatchInfoResponse

interface MatchDataSource {

    suspend fun registerMatch(runningDistanceRequest: RunningDistanceRequest): ApiResponse<MatchStatusResponse>
    suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest): ApiResponse<MatchStatusResponse>
    suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest): ApiResponse<MatchStatusResponse>
    suspend fun getRunnerIds(): ApiResponse<MatchInfoResponse>
    suspend fun cancelMatchWaitingEvent(): ApiResponse<CancelMatchResponse>

    fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit, onFailure: () -> Unit): EventSourceListener
    fun createEventSource(url: String, listener: EventSourceListener): EventSource
    fun connectWaitingEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun disconnectWaitingEventSource()
    fun disconnectMatchResultEventSource()

}
