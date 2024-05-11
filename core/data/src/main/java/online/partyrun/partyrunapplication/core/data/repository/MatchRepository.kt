package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.model.match.CancelMatch
import online.partyrun.partyrunapplication.core.model.match.MatchDecision
import online.partyrun.partyrunapplication.core.model.match.MatchStatus
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.model.match.RunningDistance

interface MatchRepository {

    suspend fun setRunners(runners: RunnerInfoData)


    /* REST */
    suspend fun registerMatch(runningDistance: RunningDistance): Result<MatchStatus>
    suspend fun acceptMatch(matchDecision: MatchDecision): Result<MatchStatus>
    suspend fun declineMatch(matchDecision: MatchDecision): Result<MatchStatus>
    suspend fun getRunnerIds(): Result<RunnerIds>
    suspend fun cancelMatchWaitingEvent(): Result<CancelMatch>

    /* SSE */
    fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit, onFailure: () -> Unit): EventSourceListener
    fun createWaitingEventSource(listener: EventSourceListener): EventSource
    fun createMatchResultEventSource(listener: EventSourceListener): EventSource

    fun connectWaitingEventSource(eventSource: EventSource)
    fun connectMatchResultEventSource(eventSource: EventSource)
    fun disconnectWaitingEventSource()
    fun disconnectMatchResultEventSource()
}
