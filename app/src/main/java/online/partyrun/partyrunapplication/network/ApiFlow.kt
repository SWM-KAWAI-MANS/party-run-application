package online.partyrun.partyrunapplication.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeoutOrNull

fun<T : Any> apiRequestFlow(call: suspend () -> ApiResult<T>): Flow<ApiResponse<T>> = flow {
    emit(ApiResponse.Loading)

    withTimeoutOrNull(20000L) {
        call()
            .onSuccess {
                emit(ApiResponse.Success(it))
            }
            .onException { it ->
                emit(ApiResponse.Failure(it.message ?: it.toString(), 400))
            }
    } ?: emit(ApiResponse.Failure("Timeout! Please try again.", 408))
}.flowOn(Dispatchers.IO)
