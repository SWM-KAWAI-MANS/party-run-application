package online.partyrun.partyrunapplication.core.domain.running

import online.partyrun.partyrunapplication.core.data.repository.BattleRepository
import javax.inject.Inject

class TerminateOngoingBattleUseCase @Inject constructor(
    private val battleRepository: BattleRepository
) {
    suspend operator fun invoke() =
        battleRepository.terminateOngoingBattle()

}