package online.partyrun.partyrunapplication.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.TestQuestionItem
import online.partyrun.partyrunapplication.core.domain.repository.TestRepository
import javax.inject.Inject

class TestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<TestQuestionItem>>> =
        testRepository.getAllQuestions()
}