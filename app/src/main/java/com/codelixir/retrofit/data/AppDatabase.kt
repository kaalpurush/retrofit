package com.codelixir.retrofit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GitHubData::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var dbInstance: AppDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppDatabase {
            if (dbInstance == null) {
                dbInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app.db"
                )
                    .fallbackToDestructiveMigration() //we want to destroy all the tables if migration fails
                    .build()
            }
            return dbInstance!!
        }
    }

    abstract fun gitHubDao(): GitHubDao
}

