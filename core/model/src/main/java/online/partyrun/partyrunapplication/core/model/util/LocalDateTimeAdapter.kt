package online.partyrun.partyrunapplication.core.model.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    override fun serialize(src: LocalDateTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return JsonPrimitive(src?.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalDateTime {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        return LocalDateTime.parse(json?.asString, formatter)
    }
}
