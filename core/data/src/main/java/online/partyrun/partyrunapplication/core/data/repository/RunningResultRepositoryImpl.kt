package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.network.datasource.RunningResultDataSource
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import javax.inject.Inject

class RunningResultRepositoryImpl @Inject constructor(
    private val runningResultDataSource: RunningResultDataSource
) : RunningResultRepository {

    override suspend fun getBattleResults(battleId: String): Flow<ApiResponse<BattleResult>> {
        return apiRequestFlow { runningResultDataSource.getBattleResults(battleId) }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(apiResponse.errorMessage, apiResponse.code)
                }
            }
    }

}
