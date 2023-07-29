package online.partyrun.partyrunapplication.core.network.datasource

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.model.request.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.network.model.response.MatchStatusResponse
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import online.partyrun.partyrunapplication.core.network.model.request.RunningDistanceRequest
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.WaitingMatchApiService
import timber.log.Timber
import javax.inject.Inject

class MatchDataSourceImpl @Inject constructor(
    @SSEOkHttpClient private val okHttpClient: OkHttpClient,
    @SSERequestBuilder private val request: Request.Builder,
    private val waitingMatchApiService: WaitingMatchApiService,
    private val matchDecisionApiService: MatchDecisionApiService
) : MatchDataSource {
    private lateinit var matchEventSource: EventSource
    private lateinit var matchResultSource: EventSource

    override suspend fun registerMatch(runningDistanceRequest: RunningDistanceRequest): ApiResult<MatchStatusResponse> =
        waitingMatchApiService.registerMatch(runningDistanceRequest)

    override suspend fun acceptMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResponse> =
        matchDecisionApiService.acceptMatch(matchDecisionRequest)

    override suspend fun declineMatch(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResponse> =
        matchDecisionApiService.declineMatch(matchDecisionRequest)

    override fun createMatchEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit
    ) : EventSourceListener {
        return createEventListener(onEvent, onClosed)
    }

    override fun createEventSource(url: String, listener: EventSourceListener): EventSource {
        val request = request.url(url).build()
        return EventSources.createFactory(okHttpClient).newEventSource(request, listener)
    }

    override fun connectWaitingEventSource(eventSource: EventSource) {
        matchEventSource = eventSource
    }

    override fun connectMatchResultEventSource(eventSource: EventSource) {
        matchResultSource = eventSource
    }

    override fun disconnectWaitingEventSource() {
        if (::matchEventSource.isInitialized) {
            matchEventSource.cancel()
        }
    }

    override fun disconnectMatchResultEventSource() {
        if (::matchResultSource.isInitialized) {
            matchResultSource.cancel()
        }
    }

    private fun createEventListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit
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
                onEvent(data)
            }

            override fun onFailure(
                eventSource: EventSource,
                t: Throwable?,
                response: Response?
            ) {
                Timber.tag("Event").d("On Failure -: $response")
                Timber.tag("Event").d("On Failure -: ${response?.body?.string()}")
            }
        }
    }

}
