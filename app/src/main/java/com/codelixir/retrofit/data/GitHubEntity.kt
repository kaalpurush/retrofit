package com.codelixir.retrofit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class GitHubEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val url: String
) : BaseEntity()