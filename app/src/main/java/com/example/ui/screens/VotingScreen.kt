package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VotingScreen(
    players: List<Player>,
    selectedSuspect: Player?,
    imposterGuessOptions: List<String>,
    onSelectSuspect: (Player) -> Unit,
    onPrepareImposterGuessOptions: () -> Unit,
    onConfirmVotingResult: (imposterGuessedWord: String?) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showImposterGuessModal by remember { mutableStateOf(false) }

    if (showConfirmDialog && selectedSuspect != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "تأكيد الاتهام",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "هل اتفقت المجموعة على اتهام ${selectedSuspect.name} بأنه الجاسوس؟",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        if (selectedSuspect.role is PlayerRole.Imposter) {
                            onPrepareImposterGuessOptions()
                            showImposterGuessModal = true
                        } else {
                            onConfirmVotingResult(null)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent, contentColor = TextPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "تأكيد الاتهام", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(text = "إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(20.dp)
        )
    }

    if (showImposterGuessModal && selectedSuspect != null) {
        Dialog(onDismissRequest = {}) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(ImposterRedContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = ImposterRed,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "كشفتم الجاسوس (${selectedSuspect.name})",
                        style = MaterialTheme.typography.titleMedium,
                        color = ImposterRed,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "فرصة الجاسوس الأخيرة! خمن الكلمة السرية لسرقة الفوز:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        imposterGuessOptions.forEach { optionWord ->
                            Button(
                                onClick = {
                                    showImposterGuessModal = false
                                    onConfirmVotingResult(optionWord)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(46.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = DarkSurfaceVariant,
                                    contentColor = TextPrimary
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = optionWord,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                showImposterGuessModal = false
                                onConfirmVotingResult(null)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "لا أعرف الكلمة (استسلام)", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "التصويت والتخمين",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = DarkSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "اختر اللاعب الذي اتفقت المجموعة على أنه الجاسوس:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(14.dp)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(players) { player ->
                    val isSelected = selectedSuspect?.id == player.id

                    Surface(
                        onClick = {
                            onSelectSuspect(player)
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) PrimaryAccent.copy(alpha = 0.2f) else DarkSurface,
                        border = if (isSelected) {
                            androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryAccent)
                        } else null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("suspect_card_${player.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) PrimaryAccent else DarkSurfaceVariant
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = TextPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            if (isSelected) {
                                Surface(
                                    color = PrimaryAccent,
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "المشتبه به",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { showConfirmDialog = true },
                enabled = selectedSuspect != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_vote_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedSuspect != null) PrimaryAccent else DarkSurfaceVariant,
                    contentColor = TextPrimary,
                    disabledContainerColor = DarkSurfaceVariant,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (selectedSuspect != null) "إعلان نتيجة التصويت على ${selectedSuspect.name}" else "حدد المشتبه به أولاً",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedSuspect != null) TextPrimary else TextMuted
                )
            }
        }
    }
}
