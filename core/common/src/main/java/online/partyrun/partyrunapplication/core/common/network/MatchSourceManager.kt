package online.partyrun.partyrunapplication.core.common.network

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener

interface MatchSourceManager {
    fun getMatchEventSourceListener(onEvent: (data: String) -> Unit): EventSourceListener
    fun connectMatchEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun closeMatchEventSource()
    fun closeMatchResultEventSource()
}
