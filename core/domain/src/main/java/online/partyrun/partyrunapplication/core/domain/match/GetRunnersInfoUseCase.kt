package online.partyrun.partyrunapplication.core.domain.match

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.match.RunnerIds
import online.partyrun.partyrunapplication.core.model.match.RunnerInfoData
import javax.inject.Inject

class GetRunnersInfoUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(runnerIds: RunnerIds): Flow<ApiResponse<RunnerInfoData>> =
        memberRepository.getRunnersInfo(runnerIds)

}
