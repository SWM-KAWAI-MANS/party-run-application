package online.partyrun.partyrunapplication.core.network.api_call_adapter

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Retrofit CallAdapter는 Call을 위임하는 response타입으로 Call을 조정..
 */
class ApiResponseCallAdapter(
    private val responseType: Type
) : CallAdapter<Type, Call<ApiResponse<Type>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<Type>): Call<ApiResponse<Type>> {
        return ApiResponseCall(call)
    }
}
