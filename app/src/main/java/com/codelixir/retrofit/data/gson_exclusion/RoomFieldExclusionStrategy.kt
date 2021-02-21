package com.portonics.aaronghss.data.remote.api_client.gson_exclusion

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.codelixir.retrofit.data.gson_exclusion.GsonExcludeRoom

class RoomFieldExclusionStrategy : ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return f.getAnnotation(GsonExcludeRoom::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }
}