package com.example.nihongomaster.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun KanjiScreen(
    repository: NihongoRepository
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()
    val bookmarkedIds by repository.bookmarkedKanjiIds.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var expandedKanjiId by remember { mutableStateOf<String?>(null) }

    val allKanji = InitialData.KANJI_LIST.filter { it.level == currentLevel }

    val filteredKanji = remember(allKanji, searchQuery) {
        allKanji.filter { item ->
            searchQuery.isBlank() ||
                    item.kanji.contains(searchQuery) ||
                    item.meaningId.contains(searchQuery, ignoreCase = true) ||
                    item.meaningEn.contains(searchQuery, ignoreCase = true) ||
                    item.onyomi.any { it.contains(searchQuery) } ||
                    item.kunyomi.any { it.contains(searchQuery) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari Kanji, Onyomi, Kunyomi, arti...", fontSize = 13.sp) },
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        )

        Text(
            text = "Total ${filteredKanji.size} Kanji untuk ${currentLevel.displayName}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredKanji, key = { it.id }) { kanjiItem ->
                val isExpanded = expandedKanjiId == kanjiItem.id
                val isBookmarked = bookmarkedIds.contains(kanjiItem.id)

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {
                            expandedKanjiId = if (isExpanded) null else kanjiItem.id
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Large Kanji Character Tile
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(OchreAmber.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = kanjiItem.kanji,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OchreAmber
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (translationLang == TranslationLang.EN) kanjiItem.meaningEn else kanjiItem.meaningId,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(
                                        text = "${kanjiItem.strokes} Goresan",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "Radikal: ${kanjiItem.radical}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = { repository.toggleBookmarkKanji(kanjiItem.id) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                                    contentDescription = "Bookmark",
                                    tint = if (isBookmarked) OchreAmber else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Onyomi & Kunyomi Rows
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "音読み (Onyomi)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (kanjiItem.onyomi.isNotEmpty()) kanjiItem.onyomi.joinToString("、") else "-",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "訓読み (Kunyomi)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (kanjiItem.kunyomi.isNotEmpty()) kanjiItem.kunyomi.joinToString("、") else "-",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Jukugo Compounds
                        if (kanjiItem.jukugo.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Kata Majemuk (熟語 / Jukugo)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                                fontWeight = FontWeight.Bold
                            )
                            Column(
                                modifier = Modifier.padding(top = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                kanjiItem.jukugo.forEach { jukugo ->
                                    Surface(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = jukugo.word,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = " (${jukugo.reading})",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Spacer(modifier = Modifier.weight(1f))
                                            Text(
                                                text = if (translationLang == TranslationLang.EN) jukugo.meaningEn else jukugo.meaningId,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Example sentence
                        if (kanjiItem.exampleSentence.japanese.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            RubyText(
                                japanese = kanjiItem.exampleSentence.japanese,
                                reading = kanjiItem.exampleSentence.reading,
                                furiganaMode = furiganaMode,
                                fontSize = 13.sp
                            )
                            Text(
                                text = if (translationLang == TranslationLang.EN) kanjiItem.exampleSentence.english else kanjiItem.exampleSentence.indonesian,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        // Add to SRS Deck Action
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    repository.addOrUpdateSRSItem(
                                        SRSItem(
                                            id = "srs-k-${kanjiItem.id}",
                                            cardType = "kanji",
                                            targetId = kanjiItem.id,
                                            frontText = kanjiItem.kanji,
                                            frontSubText = "${kanjiItem.strokes} Goresan • Radikal: ${kanjiItem.radical}",
                                            backReading = "${kanjiItem.onyomi.joinToString(", ")} / ${kanjiItem.kunyomi.joinToString(", ")}",
                                            backMeaningEn = kanjiItem.meaningEn,
                                            backMeaningId = kanjiItem.meaningId,
                                            exampleJapanese = kanjiItem.exampleSentence.japanese,
                                            exampleReading = kanjiItem.exampleSentence.reading,
                                            exampleTranslation = kanjiItem.exampleSentence.indonesian,
                                            level = kanjiItem.level
                                        )
                                    )
                                }
                            ) {
                                Icon(Icons.Default.Style, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tambah ke Flashcard SRS", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
