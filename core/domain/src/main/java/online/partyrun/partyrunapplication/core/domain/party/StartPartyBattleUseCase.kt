package online.partyrun.partyrunapplication.core.domain.party

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.PartyRepository
import javax.inject.Inject

class StartPartyBattleUseCase @Inject constructor(
    private val partyRepository: PartyRepository
) {
    suspend operator fun invoke(code: String): Flow<Result<Unit>> {
        return partyRepository.startPartyBattle(code)
    }

}
