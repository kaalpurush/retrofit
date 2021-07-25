package com.codelixir.retrofit.data

import retrofit2.http.GET

interface ServerService {
    @GET("https://60fd146c1fa9e90017c70d5f.mockapi.io/api/v1/servers")
    suspend fun getList(): List<ServerEntity>
}

class ServerMockService : ServerService {
    override suspend fun getList(): List<ServerEntity> {
        return listOf(ServerEntity(1, "hmm"))
    }
}