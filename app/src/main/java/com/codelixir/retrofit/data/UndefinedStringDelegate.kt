package com.codelixir.retrofit.data

import kotlin.reflect.KProperty

class UndefinedStringDelegate {
    private var value: String? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return (this.value ?: "Undefined") as String
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        this.value = value
    }

}