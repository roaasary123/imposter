package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    players: List<Player>,
    onNavigateTo: (AppScreen) -> Unit
) {
    var showHowToPlay by remember { mutableStateOf(false) }

    if (showHowToPlay) {
        HowToPlayDialog(onDismiss = { showHowToPlay = false })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Minimal Header Section
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(PrimaryAccent.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = PrimaryAccentLight,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "لعبة الجاسوس",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp
                        ),
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "توزيع الأدوار والتخمين الذكي",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Main Actions Buttons
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Start Game Button
                    Button(
                        onClick = { onNavigateTo(AppScreen.SETUP) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .shadow(12.dp, RoundedCornerShape(18.dp), spotColor = PrimaryAccent)
                            .testTag("start_game_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary
                        ),
                        contentPadding = PaddingValues(),
                        shape = RoundedCornerShape(18.dp)
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
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(26.dp),
                                    tint = TextPrimary
                                )
                                Text(
                                    text = "بدء لعبة جديدة",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // Custom Categories Glass Button
                    Surface(
                        onClick = { onNavigateTo(AppScreen.CUSTOM_CATEGORIES) },
                        shape = RoundedCornerShape(18.dp),
                        color = DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("custom_categories_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCard,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "التصنيفات والكلمات المخصصة",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // How to Play Rules Glass Button
                    Surface(
                        onClick = { showHowToPlay = true },
                        shape = RoundedCornerShape(18.dp),
                        color = DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("how_to_play_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = PrimaryAccentLight,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "طريقة اللعب والقوانين",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Leaderboard Glass Card (If players have scores)
            if (players.any { it.score > 0 }) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Leaderboard,
                                contentDescription = null,
                                tint = GoldLight,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "جدول النقاط الحالي",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = DarkSurface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(18.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                players.sortedByDescending { it.score }.forEachIndexed { idx, player ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
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
                                                text = player.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = TextPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        Surface(
                                            color = DarkSurfaceVariant,
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text(
                                                text = "${player.score} نقطة",
                                                style = MaterialTheme.typography.labelLarge,
                                                color = GoldLight,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


