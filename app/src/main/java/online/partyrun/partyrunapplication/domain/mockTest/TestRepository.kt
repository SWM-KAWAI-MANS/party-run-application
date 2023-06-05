package online.partyrun.partyrunapplication.domain.mockTest

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.network.models.mockTest.QuestionItem

interface TestRepository {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<QuestionItem>>>
}
