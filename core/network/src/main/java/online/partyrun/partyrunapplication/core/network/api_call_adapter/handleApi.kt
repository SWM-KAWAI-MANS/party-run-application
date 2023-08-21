package online.partyrun.partyrunapplication.core.network.api_call_adapter

import online.partyrun.partyrunapplication.core.common.network.ApiResponse
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

fun <T : Any> handleApi(
    execute: () -> Response<T>
): ApiResponse<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Timber.tag("network").e("handleApi : response.isSuccessful && body not null")
            ApiResponse.Success(body)
        } else {
            Timber.tag("network").e("handleApi : response not Successful or body is null ")
            ApiResponse.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        Timber.tag("network").e("handleApi : Error HttpException code : ${e.code()} message : ${e.message}")
        ApiResponse.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        Timber.tag("network").e("handleApi : Exception message :  ${e.message}")
        ApiResponse.Exception(e)
    }
}
