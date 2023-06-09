package online.partyrun.partyrunapplication.core.data.repository

import online.partyrun.partyrunapplication.core.data.datasource.TestDataSource
import online.partyrun.partyrunapplication.core.common.network.apiRequestFlow
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testDataSource: TestDataSource
): TestRepository {
    /*
    override suspend fun getAllQuestions(): ApiResult<List<QuestionItem>> {
        return testDataSource.invoke()
    }
     */
    override suspend fun getAllQuestions() = apiRequestFlow {
        testDataSource.invoke()
    }
}
