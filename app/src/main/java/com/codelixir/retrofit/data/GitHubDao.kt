package com.codelixir.retrofit.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GitHubDao : BaseDao<GitHubData> {
    @Query("select * FROM GitHubData")
    suspend fun all(): List<GitHubData>
}