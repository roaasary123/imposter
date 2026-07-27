package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CustomCategoryEntity
import com.example.data.CustomWordEntity
import com.example.data.DefaultCategories
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val categoryDao = db.categoryDao()

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

    // Timer State
    private val _timerSeconds = MutableStateFlow(180)
    val timerSeconds: StateFlow<Int> = _timerSeconds.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null

    // Discussion Question Helper
    private val _currentQuestion = MutableStateFlow("")
    val currentQuestion: StateFlow<String> = _currentQuestion.asStateFlow()

    // Voting State
    private val _selectedSuspect = MutableStateFlow<Player?>(null)
    val selectedSuspect: StateFlow<Player?> = _selectedSuspect.asStateFlow()

    private val _imposterGuessOptions = MutableStateFlow<List<String>>(emptyList())
    val imposterGuessOptions: StateFlow<List<String>> = _imposterGuessOptions.asStateFlow()

    private val _imposterGuessedWord = MutableStateFlow<String?>(null)
    val imposterGuessedWord: StateFlow<String?> = _imposterGuessedWord.asStateFlow()

    // Results State
    private val _gameWinner = MutableStateFlow<String?>(null) // "INNOCENTS" or "IMPOSTER"
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

    fun updateTimerMinutes(minutes: Int) {
        _settings.value = _settings.value.copy(timerMinutes = minutes)
    }

    fun toggleCategorySelection(categoryId: String) {
        val selected = _settings.value.selectedCategoryIds.toMutableSet()
        if (selected.contains(categoryId)) {
            if (selected.size > 1) { // keep at least one category selected
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

    // --- Game Logic ---
    fun startNewGame() {
        viewModelScope.launch {
            // Pick a word from selected categories
            val selectedCategoryIds = _settings.value.selectedCategoryIds
            val candidateWords = mutableListOf<SecretWordItem>()
            var chosenCategoryName = ""

            // Gather builtin candidate words
            for (catId in selectedCategoryIds) {
                DefaultCategories.BUILTIN_WORDS[catId]?.let { words ->
                    candidateWords.addAll(words)
                }
            }

            // Gather custom candidate words from Room DB
            for (catId in selectedCategoryIds) {
                val dbWords = categoryDao.getWordsForCategory(catId)
                if (dbWords.isNotEmpty()) {
                    val customCat = categoryDao.getAllCustomCategories().first().find { it.id == catId }
                    val catName = customCat?.name ?: "تصنيف مخصص"
                    candidateWords.addAll(dbWords.map { SecretWordItem(it.word, it.hint.ifBlank { "كلمة مخصصة" }, catId) })
                }
            }

            if (candidateWords.isEmpty()) {
                // Fallback if somehow empty
                val fallbackWord = DefaultCategories.BUILTIN_WORDS["foods"]!!.random()
                candidateWords.add(fallbackWord)
            }

            val chosenWordItem = candidateWords.random()
            _secretWord.value = chosenWordItem.word
            _imposterHint.value = chosenWordItem.hint

            // Find category display name
            val allCatList = allCategories.value
            chosenCategoryName = allCatList.find { it.id == chosenWordItem.categoryId }?.name ?: "تصنيف عام"
            _secretCategoryName.value = chosenCategoryName

            // Create players list with current scores preserved if restarting
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

            // Assign Imposter role(s) randomly
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

            // Pick random discussion starter
            _discussionStarter.value = assignedPlayers.random()

            // Reset reveal phase
            _currentRevealIndex.value = 0
            _isRoleCardFlipped.value = false

            // Reset timer
            _timerSeconds.value = _settings.value.timerMinutes * 60
            _isTimerRunning.value = false

            // Reset voting and results
            _selectedSuspect.value = null
            _imposterGuessedWord.value = null
            _gameWinner.value = null
            _winReason.value = ""

            // Navigate to Role Reveal
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
            // All players revealed! Start discussion phase
            _currentScreen.value = AppScreen.DISCUSSION
            if (_settings.value.timerMinutes > 0) {
                startTimer()
            }
        }
    }

    // --- Discussion & Timer ---
    fun startTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timerSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _timerSeconds.value -= 1
            }
            if (_timerSeconds.value == 0) {
                _isTimerRunning.value = false
            }
        }
    }

    fun pauseTimer() {
        _isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        _timerSeconds.value = _settings.value.timerMinutes * 60
    }

    fun addTimerMinute() {
        _timerSeconds.value += 60
    }

    fun generateNewQuestion() {
        // Questions removed per user request for simplicity
    }

    // --- Voting & Guessing ---
    fun prepareVoting() {
        pauseTimer()
        _selectedSuspect.value = null
        _currentScreen.value = AppScreen.VOTING
    }

    fun selectSuspect(player: Player) {
        _selectedSuspect.value = player
    }

    fun prepareImposterGuessOptions() {
        val currentSecret = _secretWord.value
        val categoryWords = mutableListOf<String>()

        // Gather decoys
        DefaultCategories.BUILTIN_WORDS.values.flatten().forEach { categoryWords.add(it.word) }

        val decoys = categoryWords.filter { it != currentSecret }.shuffled().take(3)
        val options = (decoys + currentSecret).shuffled()
        _imposterGuessOptions.value = options
    }

    fun confirmVotingResult(imposterGuessedWord: String? = null) {
        val suspect = _selectedSuspect.value ?: return
        val isSuspectImposter = suspect.role is PlayerRole.Imposter

        val currentWord = _secretWord.value
        val playersList = _players.value.toMutableList()

        if (isSuspectImposter) {
            // Innocents caught an imposter!
            if (imposterGuessedWord != null && imposterGuessedWord.trim() == currentWord.trim()) {
                // Imposter guessed the secret word correctly and stole the win!
                _gameWinner.value = "IMPOSTER"
                _winReason.value = "كشف الأبرياء الجاسوس، لكن الجاسوس خمن الكلمة السرية (${currentWord}) بنجاح وسرق الفوز!"

                // Award points to imposter(s)
                val updatedPlayers = playersList.map { p ->
                    if (p.role is PlayerRole.Imposter) p.copy(score = p.score + 3) else p
                }
                _players.value = updatedPlayers
            } else {
                // Innocents win!
                _gameWinner.value = "INNOCENTS"
                _winReason.value = "نجح الأبرياء في كشف الجاسوس (${suspect.name}) ولم يستطع تخمين الكلمة السرية!"

                // Award points to innocents
                val updatedPlayers = playersList.map { p ->
                    if (p.role is PlayerRole.Innocent) p.copy(score = p.score + 2) else p
                }
                _players.value = updatedPlayers
            }
        } else {
            // Innocents voted out an innocent player! Imposter wins!
            _gameWinner.value = "IMPOSTER"
            val imposterNames = playersList.filter { it.role is PlayerRole.Imposter }.joinToString(", ") { it.name }
            _winReason.value = "صوت الأبرياء ضد لاعب بريء (${suspect.name})! فاز الجاسوس (${imposterNames}) بالخدعة!"

            // Award points to imposter(s)
            val updatedPlayers = playersList.map { p ->
                if (p.role is PlayerRole.Imposter) p.copy(score = p.score + 3) else p
            }
            _players.value = updatedPlayers
        }

        _currentScreen.value = AppScreen.RESULT
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

            // Auto-select newly added custom category
            toggleCategorySelection(catId)
        }
    }

    fun deleteCustomCategory(categoryId: String) {
        viewModelScope.launch {
            val categoryEntity = CustomCategoryEntity(id = categoryId, name = "", icon = "")
            categoryDao.deleteWordsForCategory(categoryId)
            categoryDao.deleteCategory(categoryEntity)

            // Remove from selected set if present
            val selected = _settings.value.selectedCategoryIds.toMutableSet()
            selected.remove(categoryId)
            if (selected.isEmpty()) {
                selected.add("foods")
            }
            _settings.value = _settings.value.copy(selectedCategoryIds = selected)
        }
    }
}
