package com.works.solutionchallange2024.service


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.works.solutionchallange2024.model.FavData

@Dao
interface FavDao {
    @Query("SELECT * FROM favorites")
    suspend fun allTags() : List<FavData>

    @Insert
    suspend fun addTag(tag : FavData)

    @Delete
    suspend fun deleteTag(tag: FavData)

}