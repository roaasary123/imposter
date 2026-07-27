package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM custom_categories ORDER BY createdAt DESC")
    fun getAllCustomCategories(): Flow<List<CustomCategoryEntity>>

    @Query("SELECT * FROM custom_words WHERE categoryId = :categoryId")
    suspend fun getWordsForCategory(categoryId: String): List<CustomWordEntity>

    @Query("SELECT * FROM custom_words")
    suspend fun getAllCustomWords(): List<CustomWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CustomCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: CustomWordEntity)

    @Delete
    suspend fun deleteCategory(category: CustomCategoryEntity)

    @Query("DELETE FROM custom_words WHERE categoryId = :categoryId")
    suspend fun deleteWordsForCategory(categoryId: String)

    @Query("DELETE FROM custom_words WHERE id = :wordId")
    suspend fun deleteWordById(wordId: Int)
}
