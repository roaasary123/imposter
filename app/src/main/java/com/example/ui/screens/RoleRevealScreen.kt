package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleRevealScreen(
    players: List<Player>,
    currentIndex: Int,
    isCardFlipped: Boolean,
    onToggleCardFlipped: () -> Unit,
    onConfirmAndNext: () -> Unit
) {
    val currentPlayer = players.getOrNull(currentIndex) ?: return
    val totalPlayers = players.size

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "كشف الأدوار",
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
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalPlayers },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PrimaryAccent,
                    trackColor = DarkSurfaceVariant
                )

                Text(
                    text = "اللاعب ${currentIndex + 1} من $totalPlayers",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(PrimaryAccent.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "سلم الهاتف إلى:",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Text(
                                text = currentPlayer.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isCardFlipped) DarkSurface else DarkSurfaceVariant)
                    .border(
                        width = 1.5.dp,
                        color = if (isCardFlipped) {
                            if (currentPlayer.role is PlayerRole.Imposter) ImposterRed else InnocentGreen
                        } else CardBorderSubtle,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .clickable { onToggleCardFlipped() }
                    .testTag("role_reveal_card")
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isCardFlipped) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(PrimaryAccent.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Text(
                            text = "انقر لكشف دورك والكلمة السرية",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "تأكد من عدم رؤية بقية اللاعبين للشاشة!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    when (val role = currentPlayer.role) {
                        is PlayerRole.Innocent -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = InnocentGreenContainer,
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, InnocentGreen.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "أنت بريء",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = InnocentGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp)
                                    )
                                }

                                Text(
                                    text = "التصنيف: ${role.categoryName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )

                                Surface(
                                    color = DarkBackground,
                                    shape = RoundedCornerShape(16.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, InnocentGreen.copy(alpha = 0.3f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "الكلمة السرية:",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = role.secretWord,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = InnocentGreen,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                                Text(
                                    text = "احفظ الكلمة وشارك في النقاش دون كشفها للجاسوس",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        is PlayerRole.Imposter -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    color = ImposterRedContainer,
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImposterRed.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "أنت الجاسوس!",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = ImposterRed,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                                    )
                                }

                                if (role.hint != null) {
                                    Surface(
                                        color = GoldPrimary.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(16.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.5f))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lightbulb,
                                                    contentDescription = null,
                                                    tint = GoldLight,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = "تلميح خاص بك:",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = GoldLight,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = role.hint,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Medium,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "لا توجد تلميحات هذه الجولة! حاول التظاهر بمعرفة الكلمة والاندماج مع الآخرين.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        null -> {}
                    }
                }
            }

            Button(
                onClick = onConfirmAndNext,
                enabled = isCardFlipped,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("confirm_role_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCardFlipped) PrimaryAccent else DarkSurfaceVariant,
                    contentColor = TextPrimary,
                    disabledContainerColor = DarkSurfaceVariant,
                    disabledContentColor = TextMuted
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = if (currentIndex < totalPlayers - 1) "إخفاء وتمرير الهاتف للاعب التالي" else "إخفاء وبدء جولة النقاش",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isCardFlipped) TextPrimary else TextMuted
                )
            }
        }
    }
}
