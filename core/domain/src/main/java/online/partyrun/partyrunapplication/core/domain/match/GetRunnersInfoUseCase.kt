package online.partyrun.partyrunapplication.core.domain.match

import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import javax.inject.Inject

class GetRunnersInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(runnerIds: RunnerIds): Result<RunnerInfoData> =
        memberRepository.getRunnersInfo(runnerIds)

}
