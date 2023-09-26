package online.partyrun.partyrunapplication.core.domain.match

import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import javax.inject.Inject

class SaveRunnersInfoUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(runnerInfoData: RunnerInfoData) {
        matchRepository.setRunners(runnerInfoData)
    }

}
