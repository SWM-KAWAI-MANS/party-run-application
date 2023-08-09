package online.partyrun.partyrunapplication.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * UserPreferences Protobuf 타입의 직렬화 및 역직렬화 작업 수행
 * 직렬화: 객체를 스트림으로 변환하는 과정
 * 역직렬화: 스트림에서 객체로 변환하는 과정
 * readFrom과 writeTo 메소드는 DataStore의 백그라운드 스레드에서 호출되므로 별도의 코루틴 디스패처를 지정할 필요 X
 */
class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
    /**
     * UserPreferences 타입의 기본 인스턴스를 제공. 데이터가 손상된 경우나 초기 상태가 필요한 경우 사용
     */
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    /**
     * UserPreferences: 입력 스트림에서 UserPreferences 객체를 읽어옴.
     * serPreferences.parseFrom(input)를 통해 입력 스트림에서 Protobuf 형식의 데이터를 역직렬화.
     * 만약 데이터 형식이 올바르지 않거나 문제가 발생하면 InvalidProtocolBufferException 예외가 발생하며, 이 경우 CorruptionException으로 래핑하여 throw.
     */
    override suspend fun readFrom(input: InputStream): UserPreferences =
        try {
            UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    /**
     * writeTo(t: UserPreferences, output: OutputStream):
     * UserPreferences 객체를 출력 스트림에 쓰는 작업 수행
     * t.writeTo(output)를 통해 객체를 직렬화하고 출력 스트림에 작성
     */
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
