package online.partyrun.partyrunapplication.core.domain.running

import kotlinx.coroutines.flow.first
import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import javax.inject.Inject

class GetBattleStatusUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke() = battleRepository.battleData.first()

}