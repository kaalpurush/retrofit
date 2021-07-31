package com.codelixir.retrofit.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

@Entity()
data class ServerEntity(
    @Expose
    @PrimaryKey
    val id: Long,

    @Expose
    val name: String,
) : BaseEntity() {

    override fun toString(): String {
        return name
    }
}