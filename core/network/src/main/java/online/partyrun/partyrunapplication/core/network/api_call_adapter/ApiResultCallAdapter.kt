package online.partyrun.partyrunapplication.core.network.api_call_adapter

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Retrofit CallAdapter는 Call을 위임하는 response타입으로 Call을 조정..
 */
class ApiResultCallAdapter(
    private val resultType: Type
) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> {
        return ApiResultCall(call)
    }
}
