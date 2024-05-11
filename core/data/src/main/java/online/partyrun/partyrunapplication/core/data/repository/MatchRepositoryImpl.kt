package online.partyrun.partyrunapplication.core.data.repository

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.toResultModel
import online.partyrun.partyrunapplication.core.datastore.datasource.BattlePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.match.CancelMatch
import online.partyrun.partyrunapplication.core.model.match.MatchDecision
import online.partyrun.partyrunapplication.core.model.match.MatchStatus
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import online.partyrun.partyrunapplication.core.network.datasource.MatchDataSource
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class MatchRepositoryImpl @Inject constructor(
    private val matchDataSource: MatchDataSource,
    private val battlePreferencesDataSource: BattlePreferencesDataSource,
) : MatchRepository {

    override suspend fun setRunners(runners: RunnerInfoData) {
        battlePreferencesDataSource.setRunners(runners)
    }

    /* REST */
    override suspend fun registerMatch(runningDistance: RunningDistance): Result<MatchStatus> {
        return matchDataSource
            .registerMatch(runningDistance.toRequestModel())
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun acceptMatch(matchDecision: MatchDecision): Result<MatchStatus> {
        return matchDataSource
            .acceptMatch(matchDecision.toRequestModel())
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun declineMatch(matchDecision: MatchDecision): Result<MatchStatus> {
        return matchDataSource
            .declineMatch(matchDecision.toRequestModel())
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun getRunnerIds(): Result<RunnerIds> {
        return matchDataSource
            .getRunnerIds()
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun cancelMatchWaitingEvent(): Result<CancelMatch> {
        return matchDataSource
            .cancelMatchWaitingEvent()
            .toResultModel { it.toDomainModel() }
    }

    override fun createMatchEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit,
    ): EventSourceListener {
        return matchDataSource.createMatchEventSourceListener(onEvent, onClosed, onFailure)
    }

    /* SSE */
    override fun createWaitingEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/waiting/event")
        return matchDataSource.createEventSource(url = url, listener = listener)
    }

    override fun createMatchResultEventSource(listener: EventSourceListener): EventSource {
        val url = BASE_URL.plus("/api/matching/event")
        return matchDataSource.createEventSource(url = url, listener = listener)
    }

    override fun connectWaitingEventSource(eventSource: EventSource) {
        matchDataSource.connectWaitingEventSource(eventSource)
    }

    override fun connectMatchResultEventSource(eventSource: EventSource) {
        matchDataSource.connectMatchResultEventSource(eventSource)
    }

    override fun disconnectWaitingEventSource() {
        matchDataSource.disconnectWaitingEventSource()
    }

    override fun disconnectMatchResultEventSource() {
        matchDataSource.disconnectMatchResultEventSource()
    }

}
