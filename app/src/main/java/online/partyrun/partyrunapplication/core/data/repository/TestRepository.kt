package online.partyrun.partyrunapplication.core.data.repository

import kotlinx.coroutines.flow.Flow
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import online.partyrun.partyrunapplication.core.model.TestQuestionItem

interface TestRepository {
    suspend fun getAllQuestions(): Flow<ApiResponse<List<TestQuestionItem>>>
}
