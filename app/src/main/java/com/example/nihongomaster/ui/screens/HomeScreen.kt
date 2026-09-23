package com.example.nihongomaster.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.R
import com.example.nihongomaster.data.InitialData
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.model.FuriganaMode
import com.example.nihongomaster.model.JLPTLevel
import com.example.nihongomaster.model.TranslationLang
import com.example.nihongomaster.ui.components.RubyText
import com.example.nihongomaster.ui.theme.*

@Composable
fun HomeScreen(
    repository: NihongoRepository,
    onNavigateTab: (String) -> Unit,
    onOpenAISensei: (String?) -> Unit,
    onStartExam: (String) -> Unit
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()
    val streakData by repository.streakData.collectAsState()
    val srsDeck by repository.srsDeck.collectAsState()

    val levelGrammar = remember(currentLevel) {
        InitialData.GRAMMAR_POINTS.filter { it.level == currentLevel }
    }
    val levelVocab = remember(currentLevel) {
        InitialData.VOCAB_LIST.filter { it.level == currentLevel }
    }
    val levelKanji = remember(currentLevel) {
        InitialData.KANJI_LIST.filter { it.level == currentLevel }
    }

    val dailyGrammar = levelGrammar.firstOrNull() ?: InitialData.GRAMMAR_POINTS.first()
    val dailyVocab = levelVocab.firstOrNull() ?: InitialData.VOCAB_LIST.first()

    val dueSRSCount = srsDeck.count { it.dueDate <= System.currentTimeMillis() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Hero Card with Generated Illustration
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.nihongo_hero_banner),
                        contentDescription = "NihongoMaster Hero Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.8f)
                                    ),
                                    startY = 60f
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            color = LacquerRed,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${currentLevel.displayName} Preparation",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mari Belajar Bahasa Jepang Hari Ini!",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Kurikulum Kanzen Master, Sou Matome & Simulasi JLPT",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // Quick Daily SRS Alert Banner
        if (dueSRSCount > 0) {
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = LacquerRed.copy(alpha = 0.1f),
                    border = ButtonDefaults.outlinedButtonBorder,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onNavigateTab("flashcards") }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(LacquerRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Style,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Ulasan SRS Siap Hari Ini",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = LacquerRed
                            )
                            Text(
                                text = "$dueSRSCount kartu siap diulang dengan Spaced Repetition",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = "Review",
                            modifier = Modifier.size(16.dp),
                            tint = LacquerRed
                        )
                    }
                }
            }
        }

        // Main Navigation Action Grid
        item {
            Text(
                text = "Pusat Pembelajaran (${currentLevel.displayName})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Grammar Hub",
                        subtitle = "${levelGrammar.size} Pola Bunpou",
                        icon = Icons.Outlined.MenuBook,
                        color = LacquerRed,
                        modifier = Modifier.weight(1f)
                    ) { onNavigateTab("grammar") }

                    QuickActionCard(
                        title = "Vocab Hub",
                        subtitle = "${levelVocab.size} Kosakata Goi",
                        icon = Icons.Outlined.Translate,
                        color = SageGreen,
                        modifier = Modifier.weight(1f)
                    ) { onNavigateTab("vocab") }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Kanji Hub",
                        subtitle = "${levelKanji.size} Kanji + Jukugo",
                        icon = Icons.Outlined.Edit,
                        color = OchreAmber,
                        modifier = Modifier.weight(1f)
                    ) { onNavigateTab("kanji") }

                    QuickActionCard(
                        title = "Simulasi JLPT",
                        subtitle = "Timer & Star Questions",
                        icon = Icons.Outlined.Timer,
                        color = IndigoSlate,
                        modifier = Modifier.weight(1f)
                    ) { onNavigateTab("exam") }
                }
            }
        }

        // Daily Grammar Point Spotlight
        item {
            Text(
                text = "Tata Bahasa Hari Ini (文法)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = LacquerRed.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Bab ${dailyGrammar.chapterNumber}: ${dailyGrammar.category}",
                                color = LacquerRed,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        IconButton(
                            onClick = { onOpenAISensei(dailyGrammar.title) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Tanya AI Sensei",
                                tint = LacquerRed,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dailyGrammar.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = if (translationLang == TranslationLang.EN) dailyGrammar.meaningEn else dailyGrammar.meaningId,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    if (dailyGrammar.examples.isNotEmpty()) {
                        val ex = dailyGrammar.examples.first()
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
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }

        // Daily Vocab Spotlight
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kosakata Hari Ini: ${dailyVocab.partOfSpeech}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        RubyText(
                            japanese = dailyVocab.word,
                            reading = dailyVocab.reading,
                            furiganaMode = furiganaMode,
                            fontSize = 20.sp
                        )
                        Text(
                            text = if (translationLang == TranslationLang.EN) dailyVocab.meaningEn else dailyVocab.meaningId,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Button(
                        onClick = { onNavigateTab("vocab") },
                        colors = ButtonDefaults.buttonColors(containerColor = SageGreen),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Buka Goi", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
