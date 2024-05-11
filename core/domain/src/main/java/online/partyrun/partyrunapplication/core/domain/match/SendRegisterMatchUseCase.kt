package online.partyrun.partyrunapplication.core.domain.match

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.MatchStatus
import online.partyrun.partyrunapplication.core.model.match.RunningDistance
import javax.inject.Inject

class SendRegisterMatchUseCase @Inject constructor(
    private val matchRepository: MatchRepository,
) {
    suspend operator fun invoke(runningDistance: RunningDistance): Result<MatchStatus> =
        matchRepository.registerMatch(runningDistance)
}
