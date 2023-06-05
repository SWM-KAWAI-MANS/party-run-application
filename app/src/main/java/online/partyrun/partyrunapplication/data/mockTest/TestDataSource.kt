package online.partyrun.partyrunapplication.data.mockTest

import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.network.api.mockTest.TestApiService
import online.partyrun.partyrunapplication.network.models.mockTest.QuestionItem
import javax.inject.Inject

class TestDataSource @Inject constructor(
    private val testApi: TestApiService
) {
    suspend operator fun invoke(): ApiResult<List<QuestionItem>> =
        testApi.getAllQuestions()
}
