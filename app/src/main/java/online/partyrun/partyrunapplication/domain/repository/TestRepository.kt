package online.partyrun.partyrunapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.network.ApiResponse
import online.partyrun.partyrunapplication.data.model.TestQuestionItem

interface TestRepository {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<TestQuestionItem>>>
}
