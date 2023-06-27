package online.partyrun.partyrunapplication.core.data.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener

interface MatchDataSource {
    fun connectEvent(url: String, listener: EventSourceListener): EventSource

}