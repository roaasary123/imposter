package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CustomCategoryEntity
import com.example.data.CustomWordEntity
import com.example.data.DefaultCategories
import com.example.data.NameGroupEntity
import com.example.data.NameGroupMemberEntity
import com.example.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val categoryDao = db.categoryDao()
    private val nameGroupDao = db.nameGroupDao()

    // Navigation & State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Settings
    private val _settings = MutableStateFlow(GameSettings())
    val settings: StateFlow<GameSettings> = _settings.asStateFlow()

    // Custom Player Names
    private val _playerNames = MutableStateFlow(
        listOf("اللاعب 1", "اللاعب 2", "اللاعب 3", "اللاعب 4")
    )
    val playerNames: StateFlow<List<String>> = _playerNames.asStateFlow()

    // Active Game Players
    private val _players = MutableStateFlow<List<Player>>(emptyList())
    val players: StateFlow<List<Player>> = _players.asStateFlow()

    // Game Metadata
    private val _secretWord = MutableStateFlow("")
    val secretWord: StateFlow<String> = _secretWord.asStateFlow()

    private val _secretCategoryName = MutableStateFlow("")
    val secretCategoryName: StateFlow<String> = _secretCategoryName.asStateFlow()

    private val _imposterHint = MutableStateFlow<String?>(null)
    val imposterHint: StateFlow<String?> = _imposterHint.asStateFlow()

    private val _discussionStarter = MutableStateFlow<Player?>(null)
    val discussionStarter: StateFlow<Player?> = _discussionStarter.asStateFlow()

    // Reveal Phase
    private val _currentRevealIndex = MutableStateFlow(0)
    val currentRevealIndex: StateFlow<Int> = _currentRevealIndex.asStateFlow()

    private val _isRoleCardFlipped = MutableStateFlow(false)
    val isRoleCardFlipped: StateFlow<Boolean> = _isRoleCardFlipped.asStateFlow()

    // Results State
    private val _gameWinner = MutableStateFlow<String?>(null)
    val gameWinner: StateFlow<String?> = _gameWinner.asStateFlow()

    private val _winReason = MutableStateFlow("")
    val winReason: StateFlow<String> = _winReason.asStateFlow()

    // Room DB Custom Categories Flow
    val customCategories: StateFlow<List<CategoryItem>> = categoryDao.getAllCustomCategories()
        .map { entities ->
            entities.map { entity ->
                val words = categoryDao.getWordsForCategory(entity.id)
                CategoryItem(
                    id = entity.id,
                    name = entity.name,
                    icon = entity.icon,
                    isCustom = true,
                    wordCount = words.size
                )
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // All available categories combined
    val allCategories: StateFlow<List<CategoryItem>> = combine(
        flowOf(DefaultCategories.BUILTIN_CATEGORIES),
        customCategories
    ) { builtin, custom ->
        builtin + custom
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultCategories.BUILTIN_CATEGORIES)

    // Name Groups
    val nameGroups: StateFlow<List<NameGroupEntity>> = nameGroupDao.getAllGroups()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // --- Settings Functions ---
    fun updatePlayerCount(count: Int) {
        val currentNames = _playerNames.value.toMutableList()
        if (count > currentNames.size) {
            for (i in (currentNames.size + 1)..count) {
                currentNames.add("اللاعب $i")
            }
        } else if (count < currentNames.size && count >= 3) {
            while (currentNames.size > count) {
                currentNames.removeAt(currentNames.size - 1)
            }
        }
        _playerNames.value = currentNames
        var imposters = _settings.value.imposterCount
        if (imposters >= count) imposters = 1
        _settings.value = _settings.value.copy(
            playerCount = count,
            imposterCount = imposters
        )
    }

    fun updateImposterCount(count: Int) {
        if (count >= 1 && count < _settings.value.playerCount) {
            _settings.value = _settings.value.copy(imposterCount = count)
        }
    }

    fun toggleHintEnabled(enabled: Boolean) {
        _settings.value = _settings.value.copy(enableHint = enabled)
    }

    fun toggleCategorySelection(categoryId: String) {
        val selected = _settings.value.selectedCategoryIds.toMutableSet()
        if (selected.contains(categoryId)) {
            if (selected.size > 1) {
                selected.remove(categoryId)
            }
        } else {
            selected.add(categoryId)
        }
        _settings.value = _settings.value.copy(selectedCategoryIds = selected)
    }

    fun updatePlayerName(index: Int, newName: String) {
        val names = _playerNames.value.toMutableList()
        if (index in names.indices) {
            names[index] = newName.ifBlank { "اللاعب ${index + 1}" }
            _playerNames.value = names
        }
    }

    fun loadPlayerNames(names: List<String>) {
        if (names.size >= 3) {
            _playerNames.value = names
            _settings.value = _settings.value.copy(playerCount = names.size)
        }
    }

    // --- Name Group Functions ---
    fun saveNameGroup(name: String) {
        viewModelScope.launch {
            val group = NameGroupEntity(name = name)
            val groupId = nameGroupDao.insertGroup(group)
            val members = _playerNames.value.mapIndexed { idx, playerName ->
                NameGroupMemberEntity(groupId = groupId, name = playerName, sortOrder = idx)
            }
            nameGroupDao.insertMembers(members)
        }
    }

    fun deleteNameGroup(group: NameGroupEntity) {
        viewModelScope.launch {
            nameGroupDao.deleteMembersForGroup(group.id)
            nameGroupDao.deleteGroup(group)
        }
    }

    fun loadGroupMembers(group: NameGroupEntity) {
        viewModelScope.launch {
            val members = nameGroupDao.getMembersForGroup(group.id)
            val names = members.sortedBy { it.sortOrder }.map { it.name }
            if (names.size >= 3) {
                loadPlayerNames(names)
            }
        }
    }

    // --- Game Logic ---
    fun startNewGame() {
        viewModelScope.launch {
            val selectedCategoryIds = _settings.value.selectedCategoryIds
            val candidateWords = mutableListOf<SecretWordItem>()
            var chosenCategoryName = ""

            for (catId in selectedCategoryIds) {
                DefaultCategories.BUILTIN_WORDS[catId]?.let { words ->
                    candidateWords.addAll(words)
                }
            }

            for (catId in selectedCategoryIds) {
                val dbWords = categoryDao.getWordsForCategory(catId)
                if (dbWords.isNotEmpty()) {
                    val customCat = categoryDao.getAllCustomCategories().first().find { it.id == catId }
                    val catName = customCat?.name ?: "تصنيف مخصص"
                    candidateWords.addAll(dbWords.map { SecretWordItem(it.word, it.hint.ifBlank { "كلمة مخصصة" }, catId) })
                }
            }

            if (candidateWords.isEmpty()) {
                val fallbackWord = DefaultCategories.BUILTIN_WORDS["foods"]!!.random()
                candidateWords.add(fallbackWord)
            }

            val chosenWordItem = candidateWords.random()
            _secretWord.value = chosenWordItem.word
            _imposterHint.value = chosenWordItem.hint

            val allCatList = allCategories.value
            chosenCategoryName = allCatList.find { it.id == chosenWordItem.categoryId }?.name ?: "تصنيف عام"
            _secretCategoryName.value = chosenCategoryName

            val names = _playerNames.value
            val existingScores = _players.value.associate { it.name to it.score }

            val newPlayers = names.mapIndexed { idx, name ->
                Player(
                    id = idx,
                    name = name,
                    score = existingScores[name] ?: 0,
                    role = null,
                    isRevealed = false
                )
            }

            val imposterIndices = newPlayers.indices.shuffled().take(_settings.value.imposterCount).toSet()

            val hintToGive = if (_settings.value.enableHint) chosenWordItem.hint else null

            val assignedPlayers = newPlayers.mapIndexed { idx, player ->
                val role = if (idx in imposterIndices) {
                    PlayerRole.Imposter(hintToGive)
                } else {
                    PlayerRole.Innocent(chosenWordItem.word, chosenCategoryName)
                }
                player.copy(role = role)
            }

            _players.value = assignedPlayers

            _discussionStarter.value = assignedPlayers.random()

            _currentRevealIndex.value = 0
            _isRoleCardFlipped.value = false

            _gameWinner.value = null
            _winReason.value = ""

            _currentScreen.value = AppScreen.ROLE_REVEAL
        }
    }

    fun toggleRoleCardFlipped() {
        _isRoleCardFlipped.value = !_isRoleCardFlipped.value
    }

    fun confirmRoleAndNextPlayer() {
        _isRoleCardFlipped.value = false
        val nextIdx = _currentRevealIndex.value + 1
        if (nextIdx < _players.value.size) {
            _currentRevealIndex.value = nextIdx
        } else {
            _currentScreen.value = AppScreen.DISCUSSION
        }
    }

    // --- Custom Category Database Operations ---
    fun addCustomCategory(name: String, icon: String, wordsWithHints: List<Pair<String, String>>) {
        viewModelScope.launch {
            val catId = "custom_${System.currentTimeMillis()}"
            val categoryEntity = CustomCategoryEntity(
                id = catId,
                name = name,
                icon = icon.ifBlank { "⭐" }
            )
            categoryDao.insertCategory(categoryEntity)

            for ((word, hint) in wordsWithHints) {
                if (word.isNotBlank()) {
                    categoryDao.insertWord(
                        CustomWordEntity(
                            categoryId = catId,
                            word = word.trim(),
                            hint = hint.trim()
                        )
                    )
                }
            }

            toggleCategorySelection(catId)
        }
    }

    fun deleteCustomCategory(categoryId: String) {
        viewModelScope.launch {
            val categoryEntity = CustomCategoryEntity(id = categoryId, name = "", icon = "")
            categoryDao.deleteWordsForCategory(categoryId)
            categoryDao.deleteCategory(categoryEntity)

            val selected = _settings.value.selectedCategoryIds.toMutableSet()
            selected.remove(categoryId)
            if (selected.isEmpty()) {
                selected.add("foods")
            }
            _settings.value = _settings.value.copy(selectedCategoryIds = selected)
        }
    }
}
