package online.partyrun.partyrunapplication.core.data.datasource

import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.model.match.MatchDecisionRequest
import online.partyrun.partyrunapplication.core.model.match.MatchStatusResult
import online.partyrun.partyrunapplication.core.model.match.UserSelectedMatchDistance

interface MatchDataSource {
    fun connectEvent(url: String, listener: EventSourceListener): EventSource

    suspend fun registerToBattleMatchingQueue(userSelectedMatchDistance: UserSelectedMatchDistance): ApiResult<MatchStatusResult>
    suspend fun acceptBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult>
    suspend fun declineBattleMatchingQueue(matchDecisionRequest: MatchDecisionRequest): ApiResult<MatchStatusResult>
}
