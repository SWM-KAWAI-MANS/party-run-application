package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.battle.BattleId
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.battle.TerminateBattle
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.running.RecordData

interface BattleRepository {

    val battleData: Flow<BattleStatus>

    suspend fun setBattleId(battleId: String)

    suspend fun getBattleId(): Flow<ApiResponse<BattleId>>
    suspend fun terminateOngoingBattle(): Flow<ApiResponse<TerminateBattle>>
    fun getBattleStream(battleId: String): Flow<BattleEvent>
    suspend fun sendRecordData(battleId: String, recordData: RecordData)
    suspend fun close()
}
