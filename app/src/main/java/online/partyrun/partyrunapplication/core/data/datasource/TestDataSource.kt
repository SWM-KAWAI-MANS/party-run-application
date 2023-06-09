package online.partyrun.partyrunapplication.core.data.datasource

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import online.partyrun.partyrunapplication.core.network.service.TestApiService
import online.partyrun.partyrunapplication.core.model.TestQuestionItem
import javax.inject.Inject

class TestDataSource @Inject constructor(
    private val testApi: TestApiService
) {
    suspend operator fun invoke(): ApiResult<List<TestQuestionItem>> =
        testApi.getAllQuestions()
}
