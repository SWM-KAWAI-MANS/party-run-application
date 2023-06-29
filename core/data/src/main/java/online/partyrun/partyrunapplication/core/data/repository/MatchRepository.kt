package online.partyrun.partyrunapplication.core.data.repository

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener

interface MatchRepository {
    fun connectWaitingRunnerEventSource(listener: EventSourceListener): EventSource
    fun connectMatchResultEventSource(listener: EventSourceListener): EventSource
}