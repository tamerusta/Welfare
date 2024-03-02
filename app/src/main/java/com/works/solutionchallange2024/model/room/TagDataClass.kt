package com.works.solutionchallange2024.model.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagDataClass(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tag_id") var tag_id: Int,
    @ColumnInfo(name = "tag_name") var tag_name: String
)