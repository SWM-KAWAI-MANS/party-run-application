package online.partyrun.partyrunapplication.data.mockTest

import online.partyrun.partyrunapplication.domain.mockTest.TestRepository
import online.partyrun.partyrunapplication.util.apiRequestFlow
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
