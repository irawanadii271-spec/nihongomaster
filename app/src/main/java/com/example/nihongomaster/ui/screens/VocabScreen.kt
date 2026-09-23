package com.example.nihongomaster.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.nihongomaster.ui.theme.OchreAmber
import com.example.nihongomaster.ui.theme.SageGreen

@Composable
fun VocabScreen(
    repository: NihongoRepository
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()
    val bookmarkedIds by repository.bookmarkedVocabIds.collectAsState()
    val masteredIds by repository.masteredVocabIds.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var showOnlyMastered by remember { mutableStateOf(false) }

    val allVocab = InitialData.VOCAB_LIST.filter { it.level == currentLevel }
    val categories = remember(allVocab) {
        listOf("Semua") + allVocab.map { it.category }.distinct()
    }

    val filteredVocab = remember(allVocab, searchQuery, selectedCategory, showOnlyMastered, masteredIds) {
        allVocab.filter { item ->
            val matchCategory = selectedCategory == "Semua" || item.category == selectedCategory
            val matchMastered = !showOnlyMastered || masteredIds.contains(item.id)
            val matchSearch = searchQuery.isBlank() ||
                    item.word.contains(searchQuery, ignoreCase = true) ||
                    item.reading.contains(searchQuery, ignoreCase = true) ||
                    item.romaji.contains(searchQuery, ignoreCase = true) ||
                    item.meaningId.contains(searchQuery, ignoreCase = true) ||
                    item.meaningEn.contains(searchQuery, ignoreCase = true)
            matchCategory && matchMastered && matchSearch
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Search bar
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
                placeholder = { Text("Cari kanji, romaji, arti...", fontSize = 13.sp) },
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

            IconButton(
                onClick = { showOnlyMastered = !showOnlyMastered },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (showOnlyMastered) SageGreen.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                    )
            ) {
                Icon(
                    imageVector = if (showOnlyMastered) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                    contentDescription = "Mastered",
                    tint = if (showOnlyMastered) SageGreen else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category Filter
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
                        selectedContainerColor = SageGreen,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Text(
            text = "Total ${filteredVocab.size} kosakata • ${masteredIds.size} dikuasai",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Vocab list
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(filteredVocab, key = { it.id }) { vocab ->
                val isBookmarked = bookmarkedIds.contains(vocab.id)
                val isMastered = masteredIds.contains(vocab.id)

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = SageGreen.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = vocab.partOfSpeech,
                                    color = SageGreen,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                IconButton(
                                    onClick = { repository.toggleMasteredVocab(vocab.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isMastered) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                                        contentDescription = "Sudah Hafal",
                                        tint = if (isMastered) SageGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { repository.toggleBookmarkVocab(vocab.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isBookmarked) Icons.Filled.Star else Icons.Outlined.StarBorder,
                                        contentDescription = "Star",
                                        tint = if (isBookmarked) OchreAmber else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                RubyText(
                                    japanese = vocab.word,
                                    reading = vocab.reading,
                                    furiganaMode = furiganaMode,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = vocab.romaji,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (vocab.pitchAccent.isNotBlank()) {
                                Text(
                                    text = "Pitch: ${vocab.pitchAccent}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (translationLang == TranslationLang.EN) vocab.meaningEn else vocab.meaningId,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (vocab.example.japanese.isNotBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    RubyText(
                                        japanese = vocab.example.japanese,
                                        reading = vocab.example.reading,
                                        furiganaMode = furiganaMode,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = if (translationLang == TranslationLang.EN) vocab.example.english else vocab.example.indonesian,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }

                        // Add to SRS Deck Button
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
                                            id = "srs-v-${vocab.id}",
                                            cardType = "vocab",
                                            targetId = vocab.id,
                                            frontText = vocab.word,
                                            frontSubText = vocab.partOfSpeech,
                                            backReading = "${vocab.reading} (${vocab.romaji})",
                                            backMeaningEn = vocab.meaningEn,
                                            backMeaningId = vocab.meaningId,
                                            exampleJapanese = vocab.example.japanese,
                                            exampleReading = vocab.example.reading,
                                            exampleTranslation = vocab.example.indonesian,
                                            level = vocab.level
                                        )
                                    )
                                }
                            ) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("SRS Deck", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
