package online.partyrun.partyrunapplication.network.api.mockTest

import online.partyrun.partyrunapplication.network.ApiResult
import online.partyrun.partyrunapplication.network.models.mockTest.QuestionItem
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface TestApiService {
    @GET("world.json")
    suspend fun getAllQuestions(): ApiResult<List<QuestionItem>>

}
