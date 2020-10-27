package com.codelixir.retrofit.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface GitHubDao : BaseDao<GitHubEntity> {
    @Query("select * FROM GitHubEntity")
    suspend fun all(): List<GitHubEntity>
}