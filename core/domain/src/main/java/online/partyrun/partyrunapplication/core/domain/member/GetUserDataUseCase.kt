package online.partyrun.partyrunapplication.core.domain.member

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.user.User
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {

    suspend operator fun invoke(): Flow<Result<User>> =
        memberRepository.getUserData()
}
