package online.partyrun.partyrunapplication.core.domain.running.battle

import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import javax.inject.Inject

class SaveBattleIdUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke(battleId: String) {
        battleRepository.setBattleId(battleId)
    }
}