package online.partyrun.partyrunapplication.core.network.api_call_adapter

import okhttp3.Request
import okio.Timeout
import online.partyrun.partyrunapplication.core.common.network.ApiResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Retrofit Response를 위임하기 위해
 * Call interface를 구현하는 Custom Retrofit Call 클래스 정의
 */
class ApiResultCall<T : Any>(
    private val call: Call<T>
) : Call<ApiResult<T>> {
    /**
     * enqueue(): 비동기적으로 request를 보내고 콜백에 응답을 알린다.
     * API 응답을 ApiResultCall클래스 콜백에 위임
     * API response를 받으려면, enqueue function의
     * OnResponse and onFailure functions을 override 해야 한다.
     */
    override fun enqueue(callback: Callback<ApiResult<T>>) {
        call.enqueue(object : Callback<T> {
            /**
             * OnResponse:
             * API 호출이 네트워크로부터 Success 또는 Failure 응답을 받으면 호출
             * 응답을 받으면, ApiResult를 얻기 위해 handleApi function을 사용할 수 있으며  콜백으로 전달 가능.
             */
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val apiResult = handleApi { response }
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }

            /**
             * OnFailure:
             * 서버와 통신하거나, 요청 생성, 응답 처리 시 오류가 발생하면 호출됨.
             * ApiResult.Exception 생성해 콜백으로 전달.
             */
            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiResult = ApiResult.Exception<T>(t)
                callback.onResponse(this@ApiResultCall, Response.success(apiResult))
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = ApiResultCall(call.clone())
    override fun request(): Request = call.request()
    override fun timeout(): Timeout = call.timeout()
    override fun isExecuted(): Boolean = call.isExecuted
    override fun isCanceled(): Boolean = call.isCanceled
    override fun cancel() { call.cancel() }
}
