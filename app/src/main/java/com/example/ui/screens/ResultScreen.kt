package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.model.AppScreen
import com.example.model.Player
import com.example.model.PlayerRole
import com.example.ui.theme.*

@Composable
fun ResultScreen(
    gameWinner: String?,
    winReason: String,
    secretWord: String,
    categoryName: String,
    players: List<Player>,
    onPlayAgain: () -> Unit,
    onNavigateTo: (AppScreen) -> Unit
) {
    val isImposterWinner = gameWinner == "IMPOSTER"

    Scaffold(
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Winner Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = if (isImposterWinner) ImposterRedContainer else InnocentGreenContainer,
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp,
                        if (isImposterWinner) ImposterRed else InnocentGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    if (isImposterWinner) ImposterRed.copy(alpha = 0.2f) else InnocentGreen.copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isImposterWinner) Icons.Default.Security else Icons.Default.Check,
                                contentDescription = null,
                                tint = if (isImposterWinner) ImposterRed else InnocentGreen,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Text(
                            text = if (isImposterWinner) "فاز الجاسوس" else "فاز الأبرياء",
                            style = MaterialTheme.typography.displayMedium,
                            color = if (isImposterWinner) ImposterRed else InnocentGreen,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Text(
                            text = winReason,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            // Secret Word & Category Reveal
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "الكلمة السرية في هذه الجولة:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )

                        Text(
                            text = secretWord,
                            style = MaterialTheme.typography.displayMedium,
                            color = PrimaryAccentLight,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )

                        Surface(
                            color = DarkSurfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                text = "التصنيف: $categoryName",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextPrimary,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Imposter Identity Reveal
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "هوية الجاسوس:",
                            style = MaterialTheme.typography.titleMedium,
                            color = ImposterRed,
                            fontWeight = FontWeight.Bold
                        )

                        players.filter { it.role is PlayerRole.Imposter }.forEach { imp ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(ImposterRedContainer, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Security,
                                        contentDescription = null,
                                        tint = ImposterRed,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = imp.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Scores Table
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Leaderboard,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "مجموع النقاط الحالية",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        players.sortedByDescending { it.score }.forEachIndexed { idx, p ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                if (idx == 0) GoldPrimary else DarkSurfaceVariant,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${idx + 1}",
                                            style = MaterialTheme.typography.labelLarge,
                                            color = if (idx == 0) DarkBackground else TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = p.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = "${p.score} نقطة",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Action CTAs
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onPlayAgain,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = PrimaryAccent)
                            .testTag("play_again_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary
                        ),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(PrimaryAccent, CyberCyan)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = TextPrimary)
                                Text(
                                    text = "جولة جديدة بنفس الإعدادات",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigateTo(AppScreen.SETUP) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(text = "الإعدادات")
                            }
                        }

                        OutlinedButton(
                            onClick = { onNavigateTo(AppScreen.HOME) },
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(text = "الرئيسية")
                            }
                        }
                    }
                }
            }
        }
    }
}


