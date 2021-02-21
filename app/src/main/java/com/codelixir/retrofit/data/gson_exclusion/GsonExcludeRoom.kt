package com.codelixir.retrofit.data.gson_exclusion

/**
 * To be used for disabling Gson Serialize and Deserialize
 * */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD,AnnotationTarget.FUNCTION)
annotation class GsonExcludeRoom