package online.partyrun.partyrunapplication.core.data.datasource

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance
import online.partyrun.partyrunapplication.core.network.di.SSEOkHttpClient
import online.partyrun.partyrunapplication.core.network.di.SSERequestBuilder
import online.partyrun.partyrunapplication.core.network.service.MatchDecisionApiService
import online.partyrun.partyrunapplication.core.network.service.WaitingBattleApiService
import javax.inject.Inject

class MatchDataSourceImpl @Inject constructor(
    @SSEOkHttpClient private val okHttpClient: OkHttpClient,
    @SSERequestBuilder private val request: Request.Builder,
    private val waitingBattleApi: WaitingBattleApiService,
    private val matchDecisionApiService: MatchDecisionApiService
) : MatchDataSource {

    override fun connectEvent(url: String, listener: EventSourceListener): EventSource {
        val request = request.url(url).build()
        return EventSources.createFactory(okHttpClient).newEventSource(request, listener)
    }

    override suspend fun registerToBattleMatchingQueue(userSelectedMatchDistance: UserSelectedMatchDistance): ApiResult<MatchStatusResult> =
        waitingBattleApi.registerToBattleMatchingQueue(userSelectedMatchDistance)

    override suspend fun acceptBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult> =
        matchDecisionApiService.acceptBattleMatchingQueue(matchDecisionRequest)

    override suspend fun declineBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult> =
        matchDecisionApiService.declineBattleMatchingQueue(matchDecisionRequest)

}
