package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.model.response.PartyCodeResponse
import online.partyrun.partyrunapplication.core.network.service.PartyApiService
import timber.log.Timber
import javax.inject.Inject

class PartyDataSourceImpl @Inject constructor(
    @SSEOkHttpClient private val okHttpClient: OkHttpClient,
    @SSERequestBuilder private val request: Request.Builder,
    private val partyApiService: PartyApiService
) : PartyDataSource {
    private lateinit var partyEventSource: EventSource

    override suspend fun createParty(runningDistanceRequest: RunningDistanceRequest): ApiResponse<PartyCodeResponse> =
        partyApiService.createParty(runningDistanceRequest)

    override suspend fun startPartyBattle(code: String): ApiResponse<Unit> =
        partyApiService.startPartyBattle(code)

    override suspend fun quitParty(code: String): ApiResponse<Unit> =
        partyApiService.quitParty(code)

    override fun createPartyEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit
    ): EventSourceListener {
        return createEventListener(onEvent, onClosed, onFailure)
    }

    override fun createEventSource(url: String, listener: EventSourceListener): EventSource {
        val request = request.url(url).build()
        return EventSources.createFactory(okHttpClient).newEventSource(request, listener)
    }

    override fun connectPartyEventSource(eventSource: EventSource) {
        partyEventSource = eventSource
    }

    override fun disconnectPartyEventSource() {
        if (::partyEventSource.isInitialized) {
            partyEventSource.cancel()
        }
    }

    private fun createEventListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit
    ): EventSourceListener {
        return object : EventSourceListener() {
            override fun onOpen(eventSource: EventSource, response: Response) {
                Timber.tag("Event").d("Connection Success")
            }

            override fun onClosed(eventSource: EventSource) {
                Timber.tag("Event").d("Connection closed")
                onClosed()
            }

            override fun onEvent(
                eventSource: EventSource,
                id: String?,
                type: String?,
                data: String,
            ) {
                Timber.tag("Event").d("onEvent")
                onEvent(data)
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                Timber.tag("Event").d("On Failure -: $t")
                Timber.tag("Event").d("On Failure -: $response")
                onFailure()
            }
        }
    }


}
