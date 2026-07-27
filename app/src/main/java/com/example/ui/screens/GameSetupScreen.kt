package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CategoryItem
import com.example.model.GameSettings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    settings: GameSettings,
    playerNames: List<String>,
    allCategories: List<CategoryItem>,
    onUpdatePlayerCount: (Int) -> Unit,
    onUpdateImposterCount: (Int) -> Unit,
    onToggleHintEnabled: (Boolean) -> Unit,
    onUpdateTimerMinutes: (Int) -> Unit,
    onToggleCategorySelection: (String) -> Unit,
    onUpdatePlayerName: (Int, String) -> Unit,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    var showPlayerNamesEdit by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "إعدادات الجولة",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = PrimaryAccentLight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            Surface(
                color = DarkSurface,
                shadowElevation = 16.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = onStartGame,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = PrimaryAccent)
                            .testTag("start_game_cta"),
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
                            Text(
                                text = "بدء اللعبة وتوزيع الأدوار",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // 1. Player Count & Names Section
                item {
                    SetupSectionCard(title = "عدد اللاعبين والأسماء") {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "عدد اللاعبين:",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )

                                CounterControl(
                                    value = settings.playerCount,
                                    min = 3,
                                    max = 15,
                                    onValueChange = onUpdatePlayerCount
                                )
                            }

                            // Toggle Edit Names Button
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(DarkSurfaceVariant)
                                    .clickable { showPlayerNamesEdit = !showPlayerNamesEdit }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = PrimaryAccentLight,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "تخصيص أسماء اللاعبين",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary
                                    )
                                }
                                Text(
                                    text = if (showPlayerNamesEdit) "إخفاء" else "تعديل",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = PrimaryAccentLight,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            AnimatedVisibility(visible = showPlayerNamesEdit) {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    playerNames.forEachIndexed { idx, name ->
                                        OutlinedTextField(
                                            value = name,
                                            onValueChange = { onUpdatePlayerName(idx, it) },
                                            label = { Text("اللاعب ${idx + 1}") },
                                            singleLine = true,
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(14.dp),
                                            colors = OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = PrimaryAccent,
                                                unfocusedBorderColor = CardBorderGlass,
                                                focusedLabelColor = PrimaryAccentLight,
                                                unfocusedLabelColor = TextSecondary,
                                                focusedTextColor = TextPrimary,
                                                unfocusedTextColor = TextPrimary
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Imposter Count Section
                item {
                    SetupSectionCard(title = "عدد الجواسيس") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "عدد الجواسيس في الجولة:",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "يفضل 1 للجماعات الصغيرة و 2 للكبيرة",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            CounterControl(
                                value = settings.imposterCount,
                                min = 1,
                                max = if (settings.playerCount >= 6) 2 else 1,
                                onValueChange = onUpdateImposterCount
                            )
                        }
                    }
                }

                // 3. Hint Option Toggle Section
                item {
                    SetupSectionCard(title = "خيار التلميح للجاسوس") {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
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
                                            .size(38.dp)
                                            .background(
                                                if (settings.enableHint) GoldPrimary.copy(alpha = 0.2f) else DarkSurfaceVariant,
                                                CircleShape
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = if (settings.enableHint) GoldLight else TextMuted,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = "تفعيل تلميح الجاسوس",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = if (settings.enableHint) "تلميحات غير مباشرة وبعيدة" else "معطل (بدون تلميح)",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = if (settings.enableHint) InnocentGreen else TextSecondary
                                        )
                                    }
                                }

                                Switch(
                                    checked = settings.enableHint,
                                    onCheckedChange = onToggleHintEnabled,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = DarkBackground,
                                        checkedTrackColor = PrimaryAccent,
                                        uncheckedThumbColor = TextSecondary,
                                        uncheckedTrackColor = DarkSurfaceVariant
                                    ),
                                    modifier = Modifier.testTag("hint_toggle_switch")
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = DarkSurfaceVariant.copy(alpha = 0.5f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderSubtle)
                            ) {
                                Text(
                                    text = if (settings.enableHint)
                                        "عند التفعيل: يحصل الجاسوس على تلميح غير مباشر وبطريقة بعيدة تسمح بالتفكير دون كشف الكلمة بوضوح."
                                    else
                                        "عند التعطيل: يرى الجاسوس رسالة «أنت الجاسوس!» فقط بدون أي تلميح، وتعتمد اللعبة على الملاحظة والذكاء الخالص.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }

                // 4. Discussion Timer Section
                item {
                    SetupSectionCard(title = "وقت النقاش والأسئلة") {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text(
                                text = "مدة المؤقت لكل جولة:",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(
                                    1 to "1 د",
                                    2 to "2 د",
                                    3 to "3 د",
                                    5 to "5 د",
                                    0 to "بدون وقت"
                                ).forEach { (minutes, label) ->
                                    val isSelected = settings.timerMinutes == minutes
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { onUpdateTimerMinutes(minutes) },
                                        label = {
                                            Text(
                                                text = label,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryAccent,
                                            selectedLabelColor = TextPrimary,
                                            containerColor = DarkSurfaceVariant,
                                            labelColor = TextPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Category Selection Grid Section
                item {
                    SetupSectionCard(title = "اختر تصنيفات الكلمات") {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text(
                                text = "يمكنك اختيار أكثر من تصنيف للجولة الواحدة:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                allCategories.chunked(2).forEach { rowCategories ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        rowCategories.forEach { category ->
                                            val isSelected = settings.selectedCategoryIds.contains(category.id)
                                            CategoryCardItem(
                                                category = category,
                                                isSelected = isSelected,
                                                onToggle = { onToggleCategorySelection(category.id) },
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
                                        if (rowCategories.size == 1) {
                                            Spacer(modifier = Modifier.weight(1f))
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

@Composable
private fun SetupSectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
private fun CounterControl(
    value: Int,
    min: Int,
    max: Int,
    onValueChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        IconButton(
            onClick = { if (value > min) onValueChange(value - 1) },
            enabled = value > min,
            modifier = Modifier
                .size(42.dp)
                .background(if (value > min) PrimaryAccent else DarkSurfaceVariant, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "إنقاص",
                tint = if (value > min) TextPrimary else TextSecondary
            )
        }

        Text(
            text = "$value",
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        IconButton(
            onClick = { if (value < max) onValueChange(value + 1) },
            enabled = value < max,
            modifier = Modifier
                .size(42.dp)
                .background(if (value < max) PrimaryAccent else DarkSurfaceVariant, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "زيادة",
                tint = if (value < max) TextPrimary else TextSecondary
            )
        }
    }
}

private fun getCategoryIconVector(iconKey: String): ImageVector {
    return when (iconKey) {
        "restaurant" -> Icons.Default.Restaurant
        "flight" -> Icons.Default.Flight
        "pets" -> Icons.Default.Pets
        "sports" -> Icons.Default.SportsSoccer
        "work" -> Icons.Default.Work
        "phone" -> Icons.Default.PhoneAndroid
        "home" -> Icons.Default.Home
        "car" -> Icons.Default.DirectionsCar
        "movie" -> Icons.Default.Movie
        "category" -> Icons.Default.Category
        else -> Icons.Default.Folder
    }
}

@Composable
private fun CategoryCardItem(
    category: CategoryItem,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) PrimaryAccent.copy(alpha = 0.2f) else DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) PrimaryAccent else CardBorderSubtle
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (isSelected) PrimaryAccent else DarkSurface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getCategoryIconVector(category.icon),
                    contentDescription = null,
                    tint = if (isSelected) TextPrimary else PrimaryAccentLight,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isSelected) TextPrimary else TextSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
                if (category.isCustom) {
                    Text(
                        text = "مخصص",
                        style = MaterialTheme.typography.labelSmall,
                        color = GoldLight
                    )
                }
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = PrimaryAccentLight,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}


