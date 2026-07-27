package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.model.AppScreen
import com.example.ui.screens.*
import com.example.ui.theme.ImposterGameTheme
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImposterGameTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val settings by viewModel.settings.collectAsState()
                val playerNames by viewModel.playerNames.collectAsState()
                val activePlayers by viewModel.players.collectAsState()
                val secretWord by viewModel.secretWord.collectAsState()
                val secretCategoryName by viewModel.secretCategoryName.collectAsState()
                val imposterHint by viewModel.imposterHint.collectAsState()
                val discussionStarter by viewModel.discussionStarter.collectAsState()
                val currentRevealIndex by viewModel.currentRevealIndex.collectAsState()
                val isRoleCardFlipped by viewModel.isRoleCardFlipped.collectAsState()
                val timerSeconds by viewModel.timerSeconds.collectAsState()
                val isTimerRunning by viewModel.isTimerRunning.collectAsState()
                val currentQuestion by viewModel.currentQuestion.collectAsState()
                val selectedSuspect by viewModel.selectedSuspect.collectAsState()
                val imposterGuessOptions by viewModel.imposterGuessOptions.collectAsState()
                val gameWinner by viewModel.gameWinner.collectAsState()
                val winReason by viewModel.winReason.collectAsState()
                val allCategories by viewModel.allCategories.collectAsState()
                val customCategories by viewModel.customCategories.collectAsState()

                when (currentScreen) {
                    AppScreen.HOME -> HomeScreen(
                        players = activePlayers,
                        onNavigateTo = { viewModel.navigateTo(it) }
                    )

                    AppScreen.SETUP -> GameSetupScreen(
                        settings = settings,
                        playerNames = playerNames,
                        allCategories = allCategories,
                        onUpdatePlayerCount = { viewModel.updatePlayerCount(it) },
                        onUpdateImposterCount = { viewModel.updateImposterCount(it) },
                        onToggleHintEnabled = { viewModel.toggleHintEnabled(it) },
                        onUpdateTimerMinutes = { viewModel.updateTimerMinutes(it) },
                        onToggleCategorySelection = { viewModel.toggleCategorySelection(it) },
                        onUpdatePlayerName = { idx, name -> viewModel.updatePlayerName(idx, name) },
                        onStartGame = { viewModel.startNewGame() },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )

                    AppScreen.ROLE_REVEAL -> RoleRevealScreen(
                        players = activePlayers,
                        currentIndex = currentRevealIndex,
                        isCardFlipped = isRoleCardFlipped,
                        onToggleCardFlipped = { viewModel.toggleRoleCardFlipped() },
                        onConfirmAndNext = { viewModel.confirmRoleAndNextPlayer() }
                    )

                    AppScreen.DISCUSSION -> DiscussionScreen(
                        starterPlayer = discussionStarter,
                        timerSeconds = timerSeconds,
                        isTimerRunning = isTimerRunning,
                        currentQuestion = currentQuestion,
                        onStartTimer = { viewModel.startTimer() },
                        onPauseTimer = { viewModel.pauseTimer() },
                        onResetTimer = { viewModel.resetTimer() },
                        onAddTimerMinute = { viewModel.addTimerMinute() },
                        onGenerateNewQuestion = { viewModel.generateNewQuestion() },
                        onProceedToVoting = { viewModel.prepareVoting() }
                    )

                    AppScreen.VOTING -> VotingScreen(
                        players = activePlayers,
                        selectedSuspect = selectedSuspect,
                        imposterGuessOptions = imposterGuessOptions,
                        onSelectSuspect = { viewModel.selectSuspect(it) },
                        onPrepareImposterGuessOptions = { viewModel.prepareImposterGuessOptions() },
                        onConfirmVotingResult = { word -> viewModel.confirmVotingResult(word) }
                    )

                    AppScreen.RESULT -> ResultScreen(
                        gameWinner = gameWinner,
                        winReason = winReason,
                        secretWord = secretWord,
                        categoryName = secretCategoryName,
                        players = activePlayers,
                        onPlayAgain = { viewModel.startNewGame() },
                        onNavigateTo = { viewModel.navigateTo(it) }
                    )

                    AppScreen.CUSTOM_CATEGORIES -> CustomCategoriesScreen(
                        customCategories = customCategories,
                        onAddCustomCategory = { name, icon, words -> viewModel.addCustomCategory(name, icon, words) },
                        onDeleteCustomCategory = { viewModel.deleteCustomCategory(it) },
                        onBack = { viewModel.navigateTo(AppScreen.HOME) }
                    )
                }
            }
        }
    }
}
