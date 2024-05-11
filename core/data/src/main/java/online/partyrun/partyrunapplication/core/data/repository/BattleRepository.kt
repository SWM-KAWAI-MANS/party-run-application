package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.battle.BattleId
import online.partyrun.partyrunapplication.core.model.battle.BattleStatus
import online.partyrun.partyrunapplication.core.model.battle.TerminateBattle
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.running.RecordData
import online.partyrun.partyrunapplication.core.common.result.Result

interface BattleRepository {

    val battleData: Flow<BattleStatus>

    suspend fun setBattleId(battleId: String)

    suspend fun getBattleId(): Result<BattleId>
    suspend fun terminateOngoingBattle(): Result<TerminateBattle>
    fun getBattleStream(battleId: String): Flow<BattleEvent>
    suspend fun sendRecordData(battleId: String, recordData: RecordData)
    suspend fun disposeSocketResources()
    suspend fun close()
}
