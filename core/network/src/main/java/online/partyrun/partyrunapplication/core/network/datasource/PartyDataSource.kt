package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.PartyCodeResponse

interface PartyDataSource {
    /* REST */
    suspend fun createParty(runningDistanceRequest: RunningDistanceRequest): ApiResponse<PartyCodeResponse>
    suspend fun startPartyBattle(code: String): ApiResponse<Unit>
    suspend fun quitParty(code: String): ApiResponse<Unit>

    /* SSE */
    fun createPartyEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit, onFailure: () -> Unit): EventSourceListener
    fun createEventSource(url: String, listener: EventSourceListener): EventSource
    fun connectPartyEventSource(eventSource: EventSource)
    fun disconnectPartyEventSource()
}