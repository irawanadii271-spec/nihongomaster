package com.example.nihongomaster.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.data.InitialData
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.model.*
import com.example.nihongomaster.ui.components.RubyText
import com.example.nihongomaster.ui.theme.LacquerRed
import com.example.nihongomaster.ui.theme.OchreAmber
import com.example.nihongomaster.ui.theme.SageGreen

@Composable
fun GrammarScreen(
    repository: NihongoRepository,
    onOpenAISensei: (String) -> Unit
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()
    val bookmarkedIds by repository.bookmarkedGrammarIds.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var expandedGrammarId by remember { mutableStateOf<String?>(null) }
    var showOnlyBookmarked by remember { mutableStateOf(false) }

    val allGrammar = InitialData.GRAMMAR_POINTS.filter { it.level == currentLevel }
    val categories = remember(allGrammar) {
        listOf("Semua") + allGrammar.map { it.category }.distinct()
    }

    val filteredGrammar = remember(allGrammar, searchQuery, selectedCategory, showOnlyBookmarked, bookmarkedIds) {
        allGrammar.filter { item ->
            val matchCategory = selectedCategory == "Semua" || item.category == selectedCategory
            val matchBookmark = !showOnlyBookmarked || bookmarkedIds.contains(item.id)
            val matchSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.meaningId.contains(searchQuery, ignoreCase = true) ||
                    item.meaningEn.contains(searchQuery, ignoreCase = true) ||
                    item.explanationId.contains(searchQuery, ignoreCase = true)
            matchCategory && matchBookmark && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search & Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Cari pola kalimat, arti, kanji...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
                trailingIcon = if (searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Hapus")
                        }
                    }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.weight(1f)
            )

            // Bookmark Filter Toggle
            IconButton(
                onClick = { showOnlyBookmarked = !showOnlyBookmarked },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (showOnlyBookmarked) OchreAmber.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Icon(
                    imageVector = if (showOnlyBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = "Bookmark",
                    tint = if (showOnlyBookmarked) OchreAmber else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category Filter Chips
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            items(categories) { cat ->
                val isSelected = cat == selectedCategory
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LacquerRed,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Results count
        Text(
            text = "Ditemukan ${filteredGrammar.size} pola tata bahasa",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Grammar List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredGrammar, key = { it.id }) { grammar ->
                val isExpanded = expandedGrammarId == grammar.id
                val isBookmarked = bookmarkedIds.contains(grammar.id)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    border = if (isExpanded) ButtonDefaults.outlinedButtonBorder else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            expandedGrammarId = if (isExpanded) null else grammar.id
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Card Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Surface(
                                color = LacquerRed.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "Bunpou #${grammar.bunpouNumber} • ${grammar.category}",
                                    color = LacquerRed,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { repository.toggleBookmarkGrammar(grammar.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Bookmark",
                                        tint = if (isBookmarked) OchreAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { onOpenAISensei(grammar.title) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = "Tanya Sensei",
                                        tint = LacquerRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = grammar.title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = if (translationLang == TranslationLang.EN) grammar.meaningEn else grammar.meaningId,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        // Expanded Details
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Divider(modifier = Modifier.padding(vertical = 8.dp))

                                // Structure / Formula
                                if (grammar.structure.isNotEmpty()) {
                                    Text(
                                        text = "Rumus Pola (接続 / Setsuzoku)",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        grammar.structure.forEach { formula ->
                                            Surface(
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(8.dp),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text(
                                                    text = formula,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                // Explanation
                                Text(
                                    text = "Penjelasan & Nuansa",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = if (translationLang == TranslationLang.EN) grammar.explanationEn else grammar.explanationId,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                                )

                                if (grammar.nuanceNotes.isNotBlank()) {
                                    Surface(
                                        color = SageGreen.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 8.dp)
                                    ) {
                                        Row(modifier = Modifier.padding(8.dp)) {
                                            Text(
                                                text = "💡 Catatan: ${grammar.nuanceNotes}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = SageGreen
                                            )
                                        }
                                    }
                                }

                                // Examples
                                Text(
                                    text = "Contoh Kalimat (例文 / Reibun)",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Column(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    grammar.examples.forEachIndexed { idx, ex ->
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(10.dp)) {
                                                RubyText(
                                                    japanese = ex.japanese,
                                                    reading = ex.reading,
                                                    furiganaMode = furiganaMode,
                                                    fontSize = 15.sp
                                                )
                                                Text(
                                                    text = if (translationLang == TranslationLang.EN) ex.english else ex.indonesian,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.padding(top = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Add to SRS Flashcards Action
                                Button(
                                    onClick = {
                                        repository.addOrUpdateSRSItem(
                                            SRSItem(
                                                id = "srs-g-${grammar.id}",
                                                cardType = "grammar",
                                                targetId = grammar.id,
                                                frontText = grammar.title,
                                                frontSubText = grammar.category,
                                                backReading = grammar.title,
                                                backMeaningEn = grammar.meaningEn,
                                                backMeaningId = grammar.meaningId,
                                                exampleJapanese = grammar.examples.firstOrNull()?.japanese ?: "",
                                                exampleReading = grammar.examples.firstOrNull()?.reading ?: "",
                                                exampleTranslation = grammar.examples.firstOrNull()?.indonesian ?: "",
                                                level = grammar.level
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Tambah ke Deck Flashcard SRS", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
