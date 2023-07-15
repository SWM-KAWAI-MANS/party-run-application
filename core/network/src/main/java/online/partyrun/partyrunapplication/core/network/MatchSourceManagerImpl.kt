package online.partyrun.partyrunapplication.core.network

import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.MatchSourceManager
import timber.log.Timber

class MatchSourceManagerImpl : MatchSourceManager {
    private lateinit var connectMatchEventSource: EventSource
    private lateinit var connectMatchResultSource: EventSource

    override fun createMatchEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit
    ): EventSourceListener {
        return createEventListener(onEvent, onClosed)
    }

    override fun connectMatchEventSource(eventSource: EventSource) {
        connectMatchEventSource = eventSource
    }

    override fun connectMatchResultEventSource(eventSource: EventSource) {
        connectMatchResultSource = eventSource
    }

    override fun disconnectMatchEventSource() {
        connectMatchEventSource.cancel()
    }

    override fun disconnectMatchResultEventSource() {
        connectMatchResultSource.cancel()
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
                Timber.tag("Event").d("On Failure -: ${response?.body?.string()}")
            }
        }
    }
}

