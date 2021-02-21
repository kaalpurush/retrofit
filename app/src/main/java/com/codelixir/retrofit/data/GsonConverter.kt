package com.codelixir.retrofit.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.portonics.aaronghss.data.remote.api_client.gson_exclusion.RoomFieldExclusionStrategy

abstract class GsonConverter<T> {

    private val gson by lazy {
        GsonBuilder()
            .setExclusionStrategies(RoomFieldExclusionStrategy())
            .create()
    }

    abstract fun getClassType(): Class<T>

    @TypeConverter
    fun fromSerializable(entry: T?): String? = entry?.let { gson.toJson(it) }

    @TypeConverter
    fun toSerializable(entryString: String?): T? =
        entryString?.let {
            gson.fromJson(
                it,
                TypeToken.get(getClassType()).type
            )
        }
}
