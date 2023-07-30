package online.partyrun.partyrunapplication.core.domain.running

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import online.partyrun.partyrunapplication.core.model.running.BattleEvent
import javax.inject.Inject

class BattleStreamUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    fun getBattleStream(battleId: String) : Flow<BattleEvent> = battleRepository.getBattleStream(battleId)
    suspend fun close() = battleRepository.close()

}
