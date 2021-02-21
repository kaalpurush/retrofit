package com.codelixir.retrofit.data

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.portonics.aaronghss.data.remote.api_client.gson_exclusion.RoomFieldExclusionStrategy

abstract class GsonListConverter<T> {

    private val gson by lazy {
        GsonBuilder()
            .setExclusionStrategies(RoomFieldExclusionStrategy())
            .create()
    }
    abstract fun getClassType():Class<T>

    @TypeConverter
    fun fromSerializableList(entry: List<T>?): String? = entry?.let { gson.toJson(it) }

    @TypeConverter
    fun toSerializableList(entryListString: String?): List<T>? =
            entryListString?.let {
                gson.fromJson(
                    it,
                    TypeToken.getParameterized(List::class.java, getClassType()).type
                )
            }
}
