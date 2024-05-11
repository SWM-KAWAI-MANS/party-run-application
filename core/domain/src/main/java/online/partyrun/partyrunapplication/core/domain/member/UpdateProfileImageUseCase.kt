package online.partyrun.partyrunapplication.core.domain.member

import okhttp3.RequestBody
import online.partyrun.partyrunapplication.core.common.result.Result
import online.partyrun.partyrunapplication.core.data.repository.MemberRepository
import javax.inject.Inject

class UpdateProfileImageUseCase @Inject constructor(
    private val memberRepository: MemberRepository,
) {
    suspend operator fun invoke(requestBody: RequestBody, fileName: String?): Result<Unit> {
        return memberRepository.updateProfileImage(requestBody, fileName)
    }

}
