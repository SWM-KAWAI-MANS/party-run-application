package online.partyrun.partyrunapplication.core.data.repository

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.data.datasource.MatchDataSource
import javax.inject.Inject


class MatchRepositoryImpl @Inject constructor(
    private val dataSource: MatchDataSource
) : MatchRepository {
    override fun connectWaitingRunnerEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/match/waiting-runner/event")
        return dataSource.connectEvent(url = url, listener = listener)
    }

    override fun connectMatchResultEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/match/event")
        return dataSource.connectEvent(url = url, listener = listener)
    }
}
