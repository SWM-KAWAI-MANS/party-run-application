package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import online.partyrun.partyrunapplication.core.network.model.response.ComprehensiveRunRecordResponse
import online.partyrun.partyrunapplication.core.network.model.response.SingleHistoryResponse
import online.partyrun.partyrunapplication.core.network.model.response.SingleResultResponse

interface ResultDataSource {
    suspend fun getBattleResults(battleId: String): ApiResponse<BattleResultResponse>
    suspend fun getSingleResults(singleId: String): ApiResponse<SingleResultResponse>
    suspend fun getComprehensiveRunRecord(): ApiResponse<ComprehensiveRunRecordResponse>
    suspend fun getSingleHistory(): ApiResponse<SingleHistoryResponse>
}
