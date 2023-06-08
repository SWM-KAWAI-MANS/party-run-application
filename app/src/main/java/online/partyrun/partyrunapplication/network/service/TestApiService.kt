package online.partyrun.partyrunapplication.network.service

import online.partyrun.partyrunapplication.data.model.TestQuestionItem
import online.partyrun.partyrunapplication.network.ApiResult
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface TestApiService {
    @GET("/question")
    suspend fun getAllQuestions(): ApiResult<List<TestQuestionItem>>

}
