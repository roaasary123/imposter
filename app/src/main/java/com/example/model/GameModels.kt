package com.example.model

enum class AppScreen {
    HOME,
    SETUP,
    ROLE_REVEAL,
    DISCUSSION,
    RESULT,
    CUSTOM_CATEGORIES
}

sealed class PlayerRole {
    data class Innocent(val secretWord: String, val categoryName: String) : PlayerRole()
    data class Imposter(val hint: String?) : PlayerRole()
}

data class Player(
    val id: Int,
    val name: String,
    var score: Int = 0,
    var role: PlayerRole? = null,
    var isRevealed: Boolean = false,
    var isEliminated: Boolean = false
)

data class SecretWordItem(
    val word: String,
    val hint: String,
    val categoryId: String
)

data class CategoryItem(
    val id: String,
    val name: String,
    val icon: String,
    val isCustom: Boolean = false,
    val wordCount: Int = 0
)

data class GameSettings(
    val playerCount: Int = 4,
    val imposterCount: Int = 1,
    val enableHint: Boolean = true,
    val selectedCategoryIds: Set<String> = setOf("foods", "cities", "animals", "jobs")
)
