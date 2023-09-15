package online.partyrun.partyrunapplication.core.domain.member

import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import online.partyrun.partyrunapplication.core.model.user.User
import javax.inject.Inject

class SaveUserDataUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke(userData: User) {
        memberRepository.setUserId(userData.id)
        memberRepository.setUserName(userData.nickName)
        memberRepository.setUserProfile(userData.profileImage)
    }
}
