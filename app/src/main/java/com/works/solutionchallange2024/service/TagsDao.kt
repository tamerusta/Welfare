package com.works.solutionchallange2024.service


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.works.solutionchallange2024.model.room.TagDataClass

@Dao
interface TagsDao {
    @Query("SELECT * FROM tags")
    suspend fun allTags() : List<TagDataClass>

    @Insert
    suspend fun addTag(tag : TagDataClass)

    @Delete
    suspend fun deleteTag(tag: TagDataClass)

    @Query("SELECT * FROM tags WHERE tag_name like '%' ||:searchWord||'%'")
    suspend fun searchTag(searchWord : String) : List<TagDataClass>

    @Query("SELECT * FROM tags WHERE tag_id=:tagid")
    suspend fun getTag(tagid : Int) : TagDataClass

    @Query("DELETE FROM tags")
    suspend fun deleteAllTags()
}