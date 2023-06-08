package online.partyrun.partyrunapplication.core.common.network


/**
 * 각 시나리오는 Retrofit API 호출의 서로 다른 API 결과를 나타낸다.
 * ApiResult.Success : 본문 데이터가 포함된 응답을 성공적으로 수신한 네트워크 결과
 * ApiResult.Error : 오류 메시지가 포함된 응답을 성공적으로 수신한 네트워크 결과
 * ApiResult.Exception : IOException 및 UnKnownHostException 과 같은 네트워크로부터 응답을 받기 전에 예기치 않은 예외가 발생한 네트워크 결과
 */
/**
 * Api 결과를 ApiResult 클래스로 해놓으면 API를 호출할 때마다 성공/실패 여부를 확인하고 성공 또는 실패일 때 처리하는 코드를 따로 작성하지 않아도 됨.
 * 성공 시 -> ApiResult.Success<T> 반환
 * 실패 시 -> ApiResult.Error<T> 반환
 */
sealed interface ApiResult<T : Any> {
    class Success<T : Any>(val data: T) : ApiResult<T>
    class Error<T : Any>(val code: Int, val message: String?) : ApiResult<T>
    class Exception<T : Any>(val e: Throwable) : ApiResult<T>
}

/**
 * ApiResult 클래스에 대한 확장 함수 onSuccess()
 * ApiResult의 인스턴스가 Success인 경우에만 파라미터로 주어진 action 수행
 * 수행 후 ApiResult 인스턴스 반환
 */
suspend fun <T : Any> ApiResult<T>.onSuccess(
    action: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Success<T>) {
        action(data)
    }
}

/**
 * ApiResult 클래스에 대한 확장 함수 onError()
 * ApiResult의 인스턴스가 Error인 경우에만 파라미터로 주어진 action 수행
 * 수행 후 ApiResult 인스턴스 반환
 */
suspend fun <T : Any> ApiResult<T>.onError(
    action: suspend (code: Int, message: String?) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Error<T>) {
        action(code, message)
    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    action: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiResult.Exception<T>) {
        action(e)
    }
}
