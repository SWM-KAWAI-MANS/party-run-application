package online.partyrun.partyrunapplication.network.api_call_adapter

import online.partyrun.partyrunapplication.network.ApiResult
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

fun <T : Any> handleApi(
    execute: () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Timber.tag("network").d("handleApi : response.isSuccessful && body not null")
            ApiResult.Success(body)
        } else {
            Timber.tag("network").d("handleApi : response not Successful or body is null ")
            ApiResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        Timber.tag("network").e("handleApi : Error HttpException code : ${e.code()} message : ${e.message}")
        ApiResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Timber.tag("network").e("handleApi : Exception message :  ${e.message}")
        ApiResult.Exception(e)
    }
}
