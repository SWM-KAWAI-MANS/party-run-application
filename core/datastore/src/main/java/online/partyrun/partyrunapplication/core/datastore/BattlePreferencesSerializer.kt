package online.partyrun.partyrunapplication.core.datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class BattlePreferencesSerializer @Inject constructor() : Serializer<BattlePreferences> {

    override val defaultValue: BattlePreferences = BattlePreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): BattlePreferences =
        try {
            BattlePreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: BattlePreferences, output: OutputStream) {
        t.writeTo(output)
    }

}
