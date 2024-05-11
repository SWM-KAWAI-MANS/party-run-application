package online.partyrun.partyrunapplication.core.data.repository

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.model.party.PartyCode

interface PartyRepository {
    /* REST */
    suspend fun createParty(runningDistance: RunningDistance): Result<PartyCode>
    suspend fun startPartyBattle(code: String): Result<Unit>
    suspend fun quitParty(code: String): Result<Unit>

    /* SSE */
    fun createPartyEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit,
    ): EventSourceListener

    fun createPartyEventSource(listener: EventSourceListener, code: String): EventSource

    fun connectPartyEventSource(eventSource: EventSource)
    fun disconnectPartyEventSource()
}

