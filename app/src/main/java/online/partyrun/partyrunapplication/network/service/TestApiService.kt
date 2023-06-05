package online.partyrun.partyrunapplication.network.service

import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.data.model.TestQuestionItem
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface TestApiService {
    @GET("world.json")
    suspend fun getAllQuestions(): ApiResult<List<TestQuestionItem>>

}
