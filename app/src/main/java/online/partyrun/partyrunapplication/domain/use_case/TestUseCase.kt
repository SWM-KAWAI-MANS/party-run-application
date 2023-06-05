package online.partyrun.partyrunapplication.domain.use_case

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.data.model.TestQuestionItem
import online.partyrun.partyrunapplication.domain.repository.TestRepository
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<TestQuestionItem>>> =
        testRepository.getAllQuestions()
}