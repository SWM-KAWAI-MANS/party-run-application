package online.partyrun.partyrunapplication.core.domain.match

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MatchRepository
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import javax.inject.Inject

class GetRunnerIdsUseCase @Inject constructor(
    private val matchRepository: MatchRepository
) {
    suspend operator fun invoke(): Flow<Result<RunnerIds>> =
        matchRepository.getRunnerIds()
}
