package com.example.nihongomaster.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.model.*
import com.example.nihongomaster.ui.components.RubyText
import com.example.nihongomaster.ui.theme.*

enum class StudyMode(val label: String) {
    SRS_FLASHCARD("SRS Flashcard"),
    LEARN_QUIZ("Pilihan Ganda"),
    MATCH_GAME("Cocok Kata (Match)")
}

@Composable
fun FlashcardScreen(
    repository: NihongoRepository
) {
    val srsDeck by repository.srsDeck.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()

    var activeStudyMode by remember { mutableStateOf(StudyMode.SRS_FLASHCARD) }
    var currentIndex by remember { mutableStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    // Match Game State
    var matchItems by remember { mutableStateOf<List<MatchTile>>(emptyList()) }
    var selectedTileId by remember { mutableStateOf<String?>(null) }
    var matchedPairsCount by remember { mutableStateOf(0) }

    val currentDeck = remember(srsDeck) {
        if (srsDeck.isNotEmpty()) srsDeck else com.example.nihongomaster.data.InitialData.getInitialSRSDeck()
    }

    // Reset current card if out of bounds
    val currentCard = if (currentDeck.isNotEmpty() && currentIndex in currentDeck.indices) {
        currentDeck[currentIndex]
    } else currentDeck.firstOrNull()

    // Initialize Match Game
    LaunchedEffect(activeStudyMode, srsDeck) {
        if (activeStudyMode == StudyMode.MATCH_GAME && currentDeck.isNotEmpty()) {
            val sample = currentDeck.shuffled().take(4)
            val tiles = mutableListOf<MatchTile>()
            sample.forEach { item ->
                tiles.add(MatchTile(id = "jp-${item.id}", pairId = item.id, text = item.frontText, isJapanese = true))
                tiles.add(MatchTile(id = "tr-${item.id}", pairId = item.id, text = if (translationLang == TranslationLang.EN) item.backMeaningEn else item.backMeaningId, isJapanese = false))
            }
            matchItems = tiles.shuffled()
            matchedPairsCount = 0
            selectedTileId = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Study Mode Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StudyMode.values().forEach { mode ->
                val isSelected = mode == activeStudyMode
                Button(
                    onClick = {
                        activeStudyMode = mode
                        isFlipped = false
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) LacquerRed else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text(mode.label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        when (activeStudyMode) {
            StudyMode.SRS_FLASHCARD -> {
                if (currentCard != null) {
                    // Progress & Counter
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Kartu ${currentIndex + 1} dari ${currentDeck.size}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            color = SageGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Interval: ${currentCard.intervalDays} Hari • ${currentCard.state.name}",
                                color = SageGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Interactive Flip Flashcard
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFlipped) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { isFlipped = !isFlipped }
                    ) {
                        AnimatedContent(
                            targetState = isFlipped,
                            transitionSpec = { fadeIn(tween(250)) togetherWith fadeOut(tween(250)) },
                            modifier = Modifier.fillMaxSize()
                        ) { flipped ->
                            if (!flipped) {
                                // Front Side
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Surface(
                                        color = LacquerRed.copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = currentCard.cardType.uppercase(),
                                            color = LacquerRed,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(24.dp))

                                    Text(
                                        text = currentCard.frontText,
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )

                                    if (currentCard.frontSubText.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = currentCard.frontSubText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(32.dp))
                                    Text(
                                        text = "Ketuk untuk melihat arti & bacaan ↻",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            } else {
                                // Back Side
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = currentCard.backReading,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = if (translationLang == TranslationLang.EN) currentCard.backMeaningEn else currentCard.backMeaningId,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )

                                    if (currentCard.exampleJapanese.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Surface(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                RubyText(
                                                    japanese = currentCard.exampleJapanese,
                                                    reading = currentCard.exampleReading,
                                                    furiganaMode = furiganaMode,
                                                    fontSize = 14.sp
                                                )
                                                Text(
                                                    text = currentCard.exampleTranslation,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.padding(top = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Anki SM-2 Rating Buttons (Again, Hard, Good, Easy)
                    if (isFlipped) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SRSButton(
                                label = "Ulangi",
                                interval = "1h",
                                color = LacquerRed,
                                modifier = Modifier.weight(1f)
                            ) {
                                repository.answerSRSItem(currentCard, SRSRating.AGAIN)
                                isFlipped = false
                                currentIndex = (currentIndex + 1) % currentDeck.size
                            }
                            SRSButton(
                                label = "Sulit",
                                interval = "1d",
                                color = OchreAmber,
                                modifier = Modifier.weight(1f)
                            ) {
                                repository.answerSRSItem(currentCard, SRSRating.HARD)
                                isFlipped = false
                                currentIndex = (currentIndex + 1) % currentDeck.size
                            }
                            SRSButton(
                                label = "Bagus",
                                interval = "3d",
                                color = SageGreen,
                                modifier = Modifier.weight(1f)
                            ) {
                                repository.answerSRSItem(currentCard, SRSRating.GOOD)
                                isFlipped = false
                                currentIndex = (currentIndex + 1) % currentDeck.size
                            }
                            SRSButton(
                                label = "Mudah",
                                interval = "5d",
                                color = IndigoSlate,
                                modifier = Modifier.weight(1f)
                            ) {
                                repository.answerSRSItem(currentCard, SRSRating.EASY)
                                isFlipped = false
                                currentIndex = (currentIndex + 1) % currentDeck.size
                            }
                        }
                    } else {
                        Button(
                            onClick = { isFlipped = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Buka Jawaban (Spasi / Ketuk)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            StudyMode.LEARN_QUIZ -> {
                // Multiple Choice Quiz Mode
                if (currentCard != null) {
                    val wrongOptions = remember(currentCard) {
                        currentDeck.filter { it.id != currentCard.id }.shuffled().take(3).map {
                            if (translationLang == TranslationLang.EN) it.backMeaningEn else it.backMeaningId
                        }
                    }
                    val correctAnswer = if (translationLang == TranslationLang.EN) currentCard.backMeaningEn else currentCard.backMeaningId
                    val allOptions = remember(currentCard, wrongOptions) {
                        (wrongOptions + correctAnswer).shuffled()
                    }
                    var selectedOption by remember(currentCard) { mutableStateOf<String?>(null) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = currentCard.frontText,
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Pilihlah arti yang tepat:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            allOptions.forEach { opt ->
                                val isSelected = selectedOption == opt
                                val isCorrect = opt == correctAnswer
                                val bgColor = when {
                                    selectedOption == null -> MaterialTheme.colorScheme.surfaceVariant
                                    isCorrect -> SageGreen.copy(alpha = 0.2f)
                                    isSelected -> LacquerRed.copy(alpha = 0.2f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = bgColor,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(enabled = selectedOption == null) {
                                            selectedOption = opt
                                        }
                                ) {
                                    Text(
                                        text = opt,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }

                        if (selectedOption != null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    selectedOption = null
                                    currentIndex = (currentIndex + 1) % currentDeck.size
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = LacquerRed)
                            ) {
                                Text("Lanjut ke Soal Berikutnya")
                            }
                        }
                    }
                }
            }

            StudyMode.MATCH_GAME -> {
                // Tile Matching Game
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Cocokkan pasangan Bahasa Jepang dan Artinya!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(matchItems) { tile ->
                            val isSelected = selectedTileId == tile.id
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (tile.isMatched) SageGreen.copy(alpha = 0.2f)
                                else if (isSelected) OchreAmber.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surfaceVariant,
                                border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = !tile.isMatched) {
                                        if (selectedTileId == null) {
                                            selectedTileId = tile.id
                                        } else {
                                            val first = matchItems.firstOrNull { it.id == selectedTileId }
                                            if (first != null && first.id != tile.id && first.pairId == tile.pairId) {
                                                // Matched!
                                                matchItems = matchItems.map {
                                                    if (it.id == first.id || it.id == tile.id) it.copy(isMatched = true) else it
                                                }
                                                matchedPairsCount += 1
                                            }
                                            selectedTileId = null
                                        }
                                    }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (tile.isMatched) "✓ Selesai" else tile.text,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    if (matchItems.isNotEmpty() && matchItems.all { it.isMatched }) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = SageGreen.copy(alpha = 0.15f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🎉 Luar Biasa! Semua Pasangan Cocok!", fontWeight = FontWeight.Bold, color = SageGreen)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        val sample = currentDeck.shuffled().take(4)
                                        val tiles = mutableListOf<MatchTile>()
                                        sample.forEach { item ->
                                            tiles.add(MatchTile(id = "jp-${item.id}", pairId = item.id, text = item.frontText, isJapanese = true))
                                            tiles.add(MatchTile(id = "tr-${item.id}", pairId = item.id, text = if (translationLang == TranslationLang.EN) item.backMeaningEn else item.backMeaningId, isJapanese = false))
                                        }
                                        matchItems = tiles.shuffled()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SageGreen)
                                ) {
                                    Text("Main Ronde Berikutnya")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class MatchTile(
    val id: String,
    val pairId: String,
    val text: String,
    val isJapanese: Boolean,
    val isMatched: Boolean = false
)

@Composable
private fun SRSButton(
    label: String,
    interval: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(interval, fontSize = 9.sp, color = Color.White.copy(alpha = 0.85f))
        }
    }
}
