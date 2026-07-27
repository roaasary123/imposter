package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            TopAppBar(
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryAccent,
                contentColor = TextPrimary,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = PrimaryAccent)
                    .testTag("add_custom_category_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = TextPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "إضافة تصنيف جديد", fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = DarkSurface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "ابتكر تصنيفاتك الخاصة وكلماتك السرية المخصصة مع تلميحات خاصة للعب مع العائلة والأصدقاء!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        modifier = Modifier.padding(16.dp)
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
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "لا توجد تصنيفات مخصصة حتى الآن",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "اضغط على زر الإضافة بالأسفل لإنشاء تصنيفك الأول!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        }
                    }
                }
            } else {
                items(customCategories) { cat ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = DarkSurface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(PrimaryAccent.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = PrimaryAccentLight,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = cat.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Bold
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

    // Add Custom Category Modal
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "إنشاء تصنيف مخصص جديد",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = { categoryName = it },
                            label = { Text("اسم التصنيف (مثال: أصدقاء الجامعة)") },
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

                    item {
                        Text(text = "إضافة الكلمات والتلميحات للجاسوس:", style = MaterialTheme.typography.titleMedium, color = PrimaryAccentLight)
                    }

                    items(wordsList.size) { idx ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .padding(12.dp)
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
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryAccent,
                                    unfocusedBorderColor = CardBorderGlass,
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
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PrimaryAccent,
                                    unfocusedBorderColor = CardBorderGlass,
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
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryAccentLight),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorderGlass)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "إضافة كلمة أخرى")
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
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "حفظ التصنيف", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(text = "إلغاء", color = TextSecondary)
                }
            },
            containerColor = DarkSurface,
            shape = RoundedCornerShape(28.dp)
        )
    }
}


