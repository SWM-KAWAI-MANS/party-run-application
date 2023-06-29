package online.partyrun.partyrunapplication.core.data.datasource

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import javax.inject.Inject

class MatchDataSourceImpl @Inject constructor(
    @SSEOkHttpClient private val okHttpClient: OkHttpClient,
    @SSERequestBuilder private val request: Request.Builder
) : MatchDataSource {

    override fun connectEvent(url: String, listener: EventSourceListener): EventSource {
        val request = request.url(url).build()
        return EventSources.createFactory(okHttpClient).newEventSource(request, listener)
    }
}
