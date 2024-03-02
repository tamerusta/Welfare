package com.works.solutionchallange2024.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "fav_id") var tag_id: Int,
    @ColumnInfo(name = "product_id") var product_id: String
)