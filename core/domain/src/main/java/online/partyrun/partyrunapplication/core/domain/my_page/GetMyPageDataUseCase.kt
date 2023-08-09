package online.partyrun.partyrunapplication.core.domain.my_page

import kotlinx.coroutines.flow.first
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import javax.inject.Inject

class GetMyPageDataUseCase @Inject constructor(
    private val memberRepository: MemberRepository
) {
    suspend operator fun invoke() = memberRepository.userData.first()
}
