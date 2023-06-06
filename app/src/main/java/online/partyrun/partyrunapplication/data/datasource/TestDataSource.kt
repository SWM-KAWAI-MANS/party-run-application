package online.partyrun.partyrunapplication.data.datasource

import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.network.service.TestApiService
import online.partyrun.partyrunapplication.data.model.TestQuestionItem
import javax.inject.Inject

class TestDataSource @Inject constructor(
    private val testApi: TestApiService
) {
    suspend operator fun invoke(): ApiResult<List<TestQuestionItem>> =
        testApi.getAllQuestions()
}
