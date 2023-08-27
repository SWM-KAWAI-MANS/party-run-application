package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.datastore.datasource.BattlePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.running_result.BattleResult
import online.partyrun.partyrunapplication.core.network.datasource.ResultDataSource
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.mapResultModel
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor(
    private val resultDataSource: ResultDataSource,
    private val battlePreferencesDataSource: BattlePreferencesDataSource
) : ResultRepository {

    override suspend fun getBattleResults(): Flow<Result<BattleResult>> {
        return apiRequestFlow {
            val battleId = battlePreferencesDataSource.battleData.first().battleId
            resultDataSource.getBattleResults(battleId)
        }.mapResultModel { it.toDomainModel() }
    }


}
