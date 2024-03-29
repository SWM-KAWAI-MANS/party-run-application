package online.partyrun.partyrunapplication.core.network.api_call_adapter

import okhttp3.Request
import okio.Timeout
import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Retrofit Response를 위임하기 위해
 * Call interface를 구현하는 Custom Retrofit Call 클래스 정의
 */
class ApiResponseCall<T : Any>(
    private val call: Call<T>
) : Call<ApiResponse<T>> {
    /**
     * enqueue(): 비동기적으로 request를 보내고 콜백에 응답을 알린다.
     * API 응답을 ApiResponseCall클래스 콜백에 위임
     * API response를 받으려면, enqueue function의
     * OnResponse and onFailure functions을 override 해야 한다.
     */
    override fun enqueue(callback: Callback<ApiResponse<T>>) {
        call.enqueue(object : Callback<T> {
            /**
             * OnResponse:
             * API 호출이 네트워크로부터 Success 또는 Failure 응답을 받으면 호출
             * 응답을 받으면, ApiResponse를 얻기 위해 handleApi function을 사용할 수 있으며  콜백으로 전달 가능.
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResponse = handleApi { response }
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }

            /**
             * OnFailure:
             * 서버와 통신하거나, 요청 생성, 응답 처리 시 오류가 발생하면 호출됨.
             * ApiResponse.Exception 생성해 콜백으로 전달.
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResponse = ApiResponse.Exception<T>(t)
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }
        })
    }

    override fun execute(): Response<ApiResponse<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResponse<T>> = ApiResponseCall(call.clone())
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() { call.cancel() }
}
