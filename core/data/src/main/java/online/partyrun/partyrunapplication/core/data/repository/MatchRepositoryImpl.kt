package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
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
    override suspend fun registerMatch(runningDistance: RunningDistance): Flow<ApiResponse<MatchStatus>> {
        return apiRequestFlow { matchDataSource.registerMatch(runningDistance.toRequestModel()) }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }
    override suspend fun acceptMatch(matchDecision: MatchDecision): Flow<ApiResponse<MatchStatus>> {
        return apiRequestFlow { matchDataSource.acceptMatch(matchDecision.toRequestModel()) }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

    override suspend fun declineMatch(matchDecision: MatchDecision): Flow<ApiResponse<MatchStatus>> {
        return apiRequestFlow { matchDataSource.declineMatch(matchDecision.toRequestModel()) }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

    override suspend fun getRunnerIds(): Flow<ApiResponse<RunnerIds>> {
        return apiRequestFlow { matchDataSource.getRunnerIds() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

    override suspend fun cancelMatchWaitingEvent(): Flow<ApiResponse<CancelMatch>> {
        return apiRequestFlow { matchDataSource.cancelMatchWaitingEvent() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

    override fun createMatchEventSourceListener(onEvent: (data: String) -> Unit, onClosed: () -> Unit, onFailure: () -> Unit): EventSourceListener {
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
