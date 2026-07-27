package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_categories")
data class CustomCategoryEntity(
    @PrimaryKey val id: String,
    val name: String,
    val icon: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_words")
data class CustomWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoryId: String,
    val word: String,
    val hint: String
)
