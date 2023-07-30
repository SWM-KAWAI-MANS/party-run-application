package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import online.partyrun.partyrunapplication.core.model.running.RecordData

interface BattleRepository {
    fun getBattleStream(battleId: String): Flow<BattleEvent>
    suspend fun sendRecordData(battleId: String, recordData: RecordData)
    suspend fun close()
}
