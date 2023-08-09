package online.partyrun.partyrunapplication.core.domain.member

import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import javax.inject.Inject

class SetUserProfileUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(userProfile: String) = memberRepository.setUserProfile(userProfile)
}
