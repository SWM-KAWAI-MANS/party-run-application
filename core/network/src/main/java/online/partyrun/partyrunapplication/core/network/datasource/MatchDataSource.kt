package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchInfoResponse

interface MatchDataSource {

    suspend fun registerMatch(runningDistanceRequest: RunningDistanceRequest): ApiResult<MatchStatusResponse>
    suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResponse>
    suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResponse>
    suspend fun getRunnerIds(): ApiResult<MatchInfoResponse>

    fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit, onFailure: () -> Unit): EventSourceListener
    fun createEventSource(url: String, listener: EventSourceListener): EventSource
    fun connectWaitingEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun disconnectWaitingEventSource()
    fun disconnectMatchResultEventSource()

}
