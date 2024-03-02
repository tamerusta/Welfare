package com.works.solutionchallange2024.config


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.works.solutionchallange2024.model.FavData
import com.works.solutionchallange2024.service.FavDao

@Database(entities = [FavData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavDao(): FavDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun databaseAccess(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Fav.sqlite.db"
                    )
                        .createFromAsset("Fav.sqlite.db").build()
                }
            }
            return INSTANCE
        }
    }
}