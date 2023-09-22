package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.Constants.BASE_URL
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.mapResultModel
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import online.partyrun.partyrunapplication.core.model.party.PartyCode
import online.partyrun.partyrunapplication.core.network.datasource.PartyDataSource
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class PartyRepositoryImpl @Inject constructor(
    private val partyDataSource: PartyDataSource
) : PartyRepository {

    override suspend fun createParty(runningDistance: RunningDistance): Flow<Result<PartyCode>> {
        return apiRequestFlow {
            partyDataSource.createParty(runningDistance.toRequestModel())
        }.mapResultModel { it.toDomainModel() }
    }

    override suspend fun startPartyBattle(code: String): Flow<Result<Unit>> {
        return apiRequestFlow { partyDataSource.startPartyBattle(code) }
    }

    override fun createPartyEventSourceListener(
        onEvent: (data: String) -> Unit,
        onClosed: () -> Unit,
        onFailure: () -> Unit
    ): EventSourceListener {
        return partyDataSource.createPartyEventSourceListener(onEvent, onClosed, onFailure)
    }

    override fun createPartyEventSource(listener: EventSourceListener, code: String): EventSource {
        val url = BASE_URL.plus("/api/parties/$code/join")
        return partyDataSource.createEventSource(url = url, listener = listener)
    }

    override fun connectPartyEventSource(eventSource: EventSource) {
        partyDataSource.connectPartyEventSource(eventSource)
    }

    override fun disconnectPartyEventSource() {
        partyDataSource.disconnectPartyEventSource()
    }

}
