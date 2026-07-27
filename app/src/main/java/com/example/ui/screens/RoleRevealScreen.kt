package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            // Top Progress Bar & Instruction
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalPlayers },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = PrimaryAccent,
                    trackColor = DarkSurfaceVariant
                )

                Text(
                    text = "اللاعب ${currentIndex + 1} من $totalPlayers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryAccent.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "سلم الهاتف إلى:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Text(
                                text = currentPlayer.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Center Interactive Card Flip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(if (isCardFlipped) DarkSurface else DarkSurfaceVariant)
                    .border(
                        width = 2.dp,
                        color = if (isCardFlipped) {
                            if (currentPlayer.role is PlayerRole.Imposter) ImposterRed else InnocentGreen
                        } else CardBorderGlass,
                        shape = RoundedCornerShape(28.dp)
                    )
                    .clickable { onToggleCardFlipped() }
                    .testTag("role_reveal_card")
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isCardFlipped) {
                    // Unrevealed Cover
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .background(PrimaryAccent.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Text(
                            text = "انقر لكشف دورك والكلمة السرية",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = "تأكد من عدم رؤية بقية اللاعبين للشاشة!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Revealed Secret Role Content
                    when (val role = currentPlayer.role) {
                        is PlayerRole.Innocent -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Surface(
                                    color = InnocentGreenContainer,
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, InnocentGreen.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "أنت بريء",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = InnocentGreen,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp)
                                    )
                                }

                                Text(
                                    text = "التصنيف: ${role.categoryName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )

                                Surface(
                                    color = DarkBackground,
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, InnocentGreen.copy(alpha = 0.5f))
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(18.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "الكلمة السرية:",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = TextSecondary
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = role.secretWord,
                                            style = MaterialTheme.typography.displayMedium,
                                            color = InnocentGreen,
                                            fontWeight = FontWeight.Black,
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
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Surface(
                                    color = ImposterRedContainer,
                                    shape = RoundedCornerShape(20.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, ImposterRed.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "أنت الجاسوس!",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = ImposterRed,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 22.dp, vertical = 8.dp)
                                    )
                                }

                                if (role.hint != null) {
                                    Surface(
                                        color = GoldPrimary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(20.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.8f))
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Lightbulb,
                                                    contentDescription = null,
                                                    tint = GoldLight,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Text(
                                                    text = "تلميح خاص بك:",
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = GoldLight,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = role.hint,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = "لا توجد تلميحات هذه الجولة! حاول التظاهر بمعرفة الكلمة والاندماج مع الآخرين.",
                                        style = MaterialTheme.typography.bodyMedium,
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

            // Bottom CTA Button
            Button(
                onClick = onConfirmAndNext,
                enabled = isCardFlipped,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = PrimaryAccent)
                    .testTag("confirm_role_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = TextPrimary,
                    disabledContainerColor = DarkSurfaceVariant,
                    disabledContentColor = TextMuted
                ),
                contentPadding = PaddingValues(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (isCardFlipped)
                                Brush.horizontalGradient(listOf(PrimaryAccent, CyberCyan))
                            else
                                Brush.horizontalGradient(listOf(DarkSurfaceVariant, DarkSurfaceVariant))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (currentIndex < totalPlayers - 1) "إخفاء وتمرير الهاتف للاعب التالي" else "إخفاء وبدء جولة النقاش",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isCardFlipped) TextPrimary else TextMuted
                    )
                }
            }
        }
    }
}


