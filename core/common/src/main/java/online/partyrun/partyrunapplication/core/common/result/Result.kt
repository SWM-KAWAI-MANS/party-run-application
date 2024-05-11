package online.partyrun.partyrunapplication.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import online.partyrun.partyrunapplication.core.common.network.ApiResponse

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Failure(val errorMessage: String, val code: Int) : Result<Nothing>
    object Loading : Result<Nothing>
    object Empty : Result<Nothing>  // 204나 null 대응
}

suspend fun <T> Result<T>.onSuccess(
    action: suspend (T) -> Unit
): Result<T> = apply {
    if (this is Result.Success) {
        action(data)
    }
}

suspend fun Result<*>.onFailure(
    action: suspend (errorMessage: String, code: Int) -> Unit
): Result<*> =
    apply {
        if (this is Result.Failure) {
            action(errorMessage, code)
        }
    }

suspend fun Result<*>.onEmpty(
    action: suspend () -> Unit
): Result<*> =
    apply {
        if (this is Result.Empty) {
            action()
        }
    }

/**
 * Result<R> 형식의 Flow를 받아서 그 내용을 변환한 후 Result<D> 형식의 Flow로 반환
 * Generic Parameters:
 * R: 원래의 데이터 타입.
 * D: 변환하고자 하는 목표 데이터 타입.
 * Function Parameters:
 * transform: R 타입의 데이터를 받아서 D 타입으로 변환하는 함수
 */
inline fun <reified R, reified D> Flow<Result<R>>.mapResultModel(crossinline transform: (R) -> D): Flow<Result<D>> {
    return this.map { apiResponse ->
        when (apiResponse) {
            is Result.Loading -> Result.Loading
            is Result.Success -> Result.Success(transform(apiResponse.data))
            is Result.Failure -> Result.Failure(apiResponse.errorMessage, apiResponse.code)
            is Result.Empty -> Result.Empty
        }
    }
}

suspend fun <T : Any, R> ApiResponse<T>.toResultModel(transform: suspend (T) -> R?): Result<R> {
    return when (this) {
        is ApiResponse.Success -> {
            val transformedResult = runCatching { transform(data) }.getOrNull()
            if (transformedResult != null) {
                Result.Success(transformedResult)
            } else {
                Result.Failure("Transformation resulted in null", -1)
            }
        }

        is ApiResponse.Error -> Result.Failure(message ?: "Error", code)
        is ApiResponse.Exception -> Result.Failure("Api Exception: ${e.message}", -1)
    }
}
