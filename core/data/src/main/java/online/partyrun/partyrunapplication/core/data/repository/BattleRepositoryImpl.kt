package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import online.partyrun.partyrunapplication.core.datastore.datasource.BattlePreferencesDataSource
import online.partyrun.partyrunapplication.core.model.battle.BattleId
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.battle.TerminateBattle
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.running.RecordData
import online.partyrun.partyrunapplication.core.network.RealtimeBattleClient
import online.partyrun.partyrunapplication.core.network.datasource.BattleDataSource
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.response.toDomainModel
import online.partyrun.partyrunapplication.core.network.model.toDomainModel
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val realtimeBattleClient: RealtimeBattleClient,
    private val battleDataSource: BattleDataSource,
    private val battlePreferencesDataSource: BattlePreferencesDataSource
    ) : BattleRepository {

    override val battleData: Flow<BattleStatus> =
        battlePreferencesDataSource.battleData

    override suspend fun setBattleId(battleId: String) {
        battlePreferencesDataSource.setBattleId(battleId)
    }

    override suspend fun getBattleId(): Flow<ApiResponse<BattleId>> {
        return apiRequestFlow { battleDataSource.getBattleId() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(
                        apiResponse.errorMessage,
                        apiResponse.code
                    )
                }
            }
    }

    override suspend fun terminateOngoingBattle(): Flow<ApiResponse<TerminateBattle>> {
        return apiRequestFlow { battleDataSource.terminateOngoingBattle() }
            .map { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Loading -> ApiResponse.Loading
                    is ApiResponse.Success -> ApiResponse.Success(apiResponse.data.toDomainModel())
                    is ApiResponse.Failure -> ApiResponse.Failure(
                        apiResponse.errorMessage,
                        apiResponse.code
                    )
                }
            }
    }

    override fun getBattleStream(battleId: String) : Flow<BattleEvent> =
        realtimeBattleClient.getBattleStream(battleId).map { it.toDomainModel() }

    override suspend fun sendRecordData(battleId: String, recordData: RecordData) =
        realtimeBattleClient.sendRecordData(battleId, recordData.toRequestModel())

    override suspend fun close() = realtimeBattleClient.close()
}
