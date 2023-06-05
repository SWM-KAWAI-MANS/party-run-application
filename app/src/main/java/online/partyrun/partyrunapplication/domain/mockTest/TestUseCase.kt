package online.partyrun.partyrunapplication.domain.mockTest

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.network.models.mockTest.QuestionItem
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<QuestionItem>>> =
        testRepository.getAllQuestions()
}