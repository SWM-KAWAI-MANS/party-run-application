package online.partyrun.partyrunapplication.data.repository

import online.partyrun.partyrunapplication.data.datasource.TestDataSource
import online.partyrun.partyrunapplication.domain.repository.TestRepository
import online.partyrun.partyrunapplication.network.apiRequestFlow
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
