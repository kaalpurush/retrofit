package com.codelixir.retrofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity()
data class GitHubEntity(
    @Expose
    @PrimaryKey
    val id: Long,
    @Expose
    val name: String,
    @Expose
    val url: String,
    @Expose
    val subscribers_count: Int?
) : BaseEntity()