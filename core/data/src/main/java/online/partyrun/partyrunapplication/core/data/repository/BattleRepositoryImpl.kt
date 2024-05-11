package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.common.result.toResultModel
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val realtimeBattleClient: RealtimeBattleClient,
    private val battleDataSource: BattleDataSource,
    private val battlePreferencesDataSource: BattlePreferencesDataSource,
) : BattleRepository {

    override val battleData: Flow<BattleStatus> =
        battlePreferencesDataSource.battleData

    override suspend fun setBattleId(battleId: String) {
        battlePreferencesDataSource.setBattleId(battleId)
    }

    override suspend fun getBattleId(): Result<BattleId> {
        return battleDataSource
            .getBattleId()
            .toResultModel { it.toDomainModel() }
    }

    override suspend fun terminateOngoingBattle(): Result<TerminateBattle> {
        return battleDataSource
            .terminateOngoingBattle()
            .toResultModel { it.toDomainModel() }
    }


    override fun getBattleStream(battleId: String): Flow<BattleEvent> =
        realtimeBattleClient.getBattleStream(battleId).map { it.toDomainModel() }

    override suspend fun sendRecordData(battleId: String, recordData: RecordData) =
        realtimeBattleClient.sendRecordData(battleId, recordData.toRequestModel())

    override suspend fun close() = realtimeBattleClient.closeSocket()
    override suspend fun disposeSocketResources() = realtimeBattleClient.disposeSocketResources()

}
