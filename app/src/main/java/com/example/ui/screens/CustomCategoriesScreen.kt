package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.CategoryItem
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCategoriesScreen(
    customCategories: List<CategoryItem>,
    onAddCustomCategory: (name: String, icon: String, wordsWithHints: List<Pair<String, String>>) -> Unit,
    onDeleteCustomCategory: (categoryId: String) -> Unit,
    onBack: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    var categoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("folder") }
    var wordsList by remember { mutableStateOf(listOf("" to "", "" to "")) }

    val iconOptions = listOf("folder", "category", "star")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "التصنيفات المخصصة",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DarkBackground)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryAccent,
                contentColor = TextPrimary,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.testTag("add_custom_category_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextPrimary)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "إضافة تصنيف جديد", fontWeight = FontWeight.SemiBold, color = TextPrimary)
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DarkSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ابتكر تصنيفاتك الخاصة وكلماتك السرية المخصصة مع تلميحات خاصة للعب مع العائلة والأصدقاء!",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            if (customCategories.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "لا توجد تصنيفات مخصصة حتى الآن",
                                style = MaterialTheme.typography.titleSmall,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "اضغط على زر الإضافة بالأسفل لإنشاء تصنيفك الأول!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(customCategories) { cat ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = DarkSurface,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryAccent.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = PrimaryAccentLight,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = cat.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${cat.wordCount} كلمة مخصصة",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PrimaryAccentLight
                                    )
                                }
                            }

                            IconButton(
                                onClick = { onDeleteCustomCategory(cat.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "حذف",
                                    tint = ImposterRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "إنشاء تصنيف مخصص جديد",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = { categoryName = it },
                            label = { Text("اسم التصنيف (مثال: أصدقاء الجامعة)") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryAccent,
                                unfocusedBorderColor = CardBorderSubtle,
                                focusedLabelColor = PrimaryAccentLight,
                                unfocusedLabelColor = TextSecondary,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    item {
                        Text(text = "إضافة الكلمات والتلميحات:", style = MaterialTheme.typography.titleSmall, color = PrimaryAccentLight)
                    }

                    items(wordsList.size) { idx ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(10.dp)
                        ) {
                            OutlinedTextField(
                                value = wordsList[idx].first,
                                onValueChange = { newWord ->
                                    val updated = wordsList.toMutableList()
                                    updated[idx] = newWord to updated[idx].second
                                    wordsList = updated
                                },
                                label = { Text("الكلمة السرية ${idx + 1}") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryAccent,
                                    unfocusedBorderColor = CardBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )

                            OutlinedTextField(
                                value = wordsList[idx].second,
                                onValueChange = { newHint ->
                                    val updated = wordsList.toMutableList()
                                    updated[idx] = updated[idx].first to newHint
                                    wordsList = updated
                                },
                                label = { Text("تلميح الجاسوس (اختياري)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryAccent,
                                    unfocusedBorderColor = CardBorderSubtle,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary
                                )
                            )
                        }
                    }

                    item {
                        OutlinedButton(
                            onClick = { wordsList = wordsList + ("" to "") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryAccentLight)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "إضافة كلمة أخرى", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (categoryName.isNotBlank()) {
                            onAddCustomCategory(categoryName, selectedIcon, wordsList)
                            categoryName = ""
                            wordsList = listOf("" to "", "" to "")
                            showAddDialog = false
                        }
                    },
                    enabled = categoryName.isNotBlank() && wordsList.any { it.first.isNotBlank() },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent, contentColor = TextPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "حفظ التصنيف", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(text = "إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(24.dp)
        )
    }
}
