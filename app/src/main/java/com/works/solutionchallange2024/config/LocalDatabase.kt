package com.works.solutionchallange2024.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.works.solutionchallange2024.model.room.TagDataClass

@Database(entities = [TagDataClass::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getTags() : TagsDao


    companion object{

        private var INSTANCE : LocalDatabase? = null

        fun databaseAccess(context: Context) : LocalDatabase? {
            if(INSTANCE == null){
                synchronized(LocalDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        LocalDatabase::class.java,
                        "searchBar.sqlite")
                        .createFromAsset("searchBar.sqlite").build()
                }
            }
            return INSTANCE
        }
    }
}