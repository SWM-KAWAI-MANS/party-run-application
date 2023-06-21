package online.partyrun.partyrunapplication.core.network.service

import online.partyrun.partyrunapplication.core.model.TestQuestionItem
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface TestApiService {
    @GET("/question")
    suspend fun getAllQuestions(): ApiResult<List<TestQuestionItem>>

}
