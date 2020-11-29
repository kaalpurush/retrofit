package com.codelixir.retrofit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GitHubEntity::class], version = 1, exportSchema = false)
@TypeConverters(
    DateConverter::class
)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var dbInstance: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            return dbInstance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app.db"
            )
                .fallbackToDestructiveMigration() //we want to destroy all the tables if migration fails
                .build()
        }
    }

    abstract fun gitHubDao(): GitHubDao
}

