package online.partyrun.partyrunapplication.core.network.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.network.model.response.BattleResultResponse
import online.partyrun.partyrunapplication.core.network.model.response.ComprehensiveRunRecordResponse
import online.partyrun.partyrunapplication.core.network.model.response.SingleHistoryResponse
import online.partyrun.partyrunapplication.core.network.model.response.SingleResultResponse
import online.partyrun.partyrunapplication.core.network.service.ResultApiService
import javax.inject.Inject

class ResultDataSourceImpl @Inject constructor(
    private val resultApi: ResultApiService
) : ResultDataSource {
    override suspend fun getBattleResults(battleId: String): ApiResponse<BattleResultResponse> =
        resultApi.getBattleResults(battleId)

    override suspend fun getSingleResults(singleId: String): ApiResponse<SingleResultResponse> =
        resultApi.getSingleResults(singleId)

    override suspend fun getComprehensiveRunRecord(): ApiResponse<ComprehensiveRunRecordResponse> =
        resultApi.getComprehensiveRunRecord()

    override suspend fun getSingleHistory(): ApiResponse<SingleHistoryResponse> =
        resultApi.getSingleHistory()

}
