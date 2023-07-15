package online.partyrun.partyrunapplication.core.common.network

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener

interface MatchSourceManager {
    fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit): EventSourceListener
    fun connectMatchEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun disconnectMatchEventSource()
    fun disconnectMatchResultEventSource()
}
