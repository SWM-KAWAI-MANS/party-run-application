package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.running.RecordData
import online.partyrun.partyrunapplication.core.network.RealtimeBattleClient
import online.partyrun.partyrunapplication.core.network.model.request.toRequestModel
import online.partyrun.partyrunapplication.core.network.model.toDomainModel
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val realtimeBattleClient: RealtimeBattleClient
) : BattleRepository {

    override fun getBattleStream(battleId: String) : Flow<BattleEvent> =
        realtimeBattleClient.getBattleStream(battleId).map { it.toDomainModel() }

    override suspend fun sendRecordData(battleId: String, recordData: RecordData) =
        realtimeBattleClient.sendRecordData(battleId, recordData.toRequestModel())

    override suspend fun close() = realtimeBattleClient.close()
}
