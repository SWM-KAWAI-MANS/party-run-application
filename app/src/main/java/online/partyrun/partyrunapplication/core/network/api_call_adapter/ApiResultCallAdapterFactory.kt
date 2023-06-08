package online.partyrun.partyrunapplication.core.network.api_call_adapter

import online.partyrun.partyrunapplication.core.common.network.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Retrofit CallAdapterFactory
 * Retrofit Builder에 대한 ApiService interface 메서드의 return 타입을 보고 CallAdapter 인스턴스를 생성함.
 */
class ApiResultCallAdapterFactory private constructor() : CallAdapter.Factory() {
    /**
     * get 메서드는 ApiService interface 메서드의 리턴 타입을 보고 적절한 CallAdapter 리턴
     * 위 NetworkResultCallAdapterFactory 클래스는 서비스 인터페이스 메서드의 반환 유형이면 NetworkResultCallAdapter 인스턴스 생성 [Call<NetworkResult<T>>]
     * ApiResultCallAdapterFactory 클래스는 ApiService interface 메서드의 리턴타입이 Call<ApiResult<T>>이면 ApiResultCallAdapter의 인스턴스 생성
     */
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != ApiResult::class.java) {
            return null
        }

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return ApiResultCallAdapter(resultType)
    }

    companion object {
        fun create(): ApiResultCallAdapterFactory = ApiResultCallAdapterFactory()
    }
}
