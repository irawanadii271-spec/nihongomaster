package com.example.nihongomaster.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.data.InitialData
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.model.JLPTLevel
import com.example.nihongomaster.ui.theme.*

@Composable
fun AnalyticsScreen(
    repository: NihongoRepository
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val streakData by repository.streakData.collectAsState()
    val masteredVocabIds by repository.masteredVocabIds.collectAsState()
    val bookmarkedGrammarIds by repository.bookmarkedGrammarIds.collectAsState()
    val bookmarkedKanjiIds by repository.bookmarkedKanjiIds.collectAsState()
    val srsDeck by repository.srsDeck.collectAsState()
    val examSubmissions by repository.examSubmissions.collectAsState()

    val totalVocabCount = InitialData.VOCAB_LIST.count { it.level == currentLevel }
    val totalGrammarCount = InitialData.GRAMMAR_POINTS.count { it.level == currentLevel }
    val totalKanjiCount = InitialData.KANJI_LIST.count { it.level == currentLevel }

    val vocabMasteredPercent = if (totalVocabCount > 0) (masteredVocabIds.size.toFloat() / totalVocabCount * 100).coerceAtMost(100f) else 40f
    val srsMasteredCount = srsDeck.count { it.state == com.example.nihongomaster.model.SRSState.MASTERED }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // Daily Streak Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(OchreAmber.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = OchreAmber,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${streakData.currentStreak} Hari Beruntun",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Rekor terpanjang: ${streakData.longestStreak} hari • Terus pertahankan!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 4 Key Metrics Grid
        item {
            Text(
                text = "Statistik Penguasaan Materi (${currentLevel.displayName})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBox(
                        title = "Kosakata (Goi)",
                        value = "${masteredVocabIds.size} / $totalVocabCount",
                        subtitle = "${vocabMasteredPercent.toInt()}% Terkuasai",
                        icon = Icons.Outlined.Translate,
                        color = SageGreen,
                        modifier = Modifier.weight(1f)
                    )

                    MetricBox(
                        title = "Tata Bahasa",
                        value = "${bookmarkedGrammarIds.size} / $totalGrammarCount",
                        subtitle = "Pola Tersimpan",
                        icon = Icons.Outlined.MenuBook,
                        color = LacquerRed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricBox(
                        title = "Kanji & Jukugo",
                        value = "${bookmarkedKanjiIds.size} / $totalKanjiCount",
                        subtitle = "Kanji Dipelajari",
                        icon = Icons.Outlined.Edit,
                        color = OchreAmber,
                        modifier = Modifier.weight(1f)
                    )

                    MetricBox(
                        title = "SRS Terhafal",
                        value = "$srsMasteredCount / ${srsDeck.size}",
                        subtitle = "Memori Jangka Panjang",
                        icon = Icons.Outlined.Style,
                        color = IndigoSlate,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Section Breakdown Progress Bars
        item {
            Text(
                text = "Akurasi per Bagian Ujian JLPT",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SectionProgressItem(name = "Kosakata & Kanji (Goi)", progress = 0.85f, color = SageGreen)
                    SectionProgressItem(name = "Tata Bahasa (Bunpou)", progress = 0.72f, color = LacquerRed)
                    SectionProgressItem(name = "Pemahaman Bacaan (Dokkai)", progress = 0.64f, color = IndigoSlate)
                    SectionProgressItem(name = "Mendengarkan (Choukai)", progress = 0.58f, color = OchreAmber)
                }
            }
        }

        // Recent Mock Exam Submissions
        item {
            Text(
                text = "Riwayat Ujian & Simulasi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (examSubmissions.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Belum ada riwayat ujian. Mulai simulasi pertama di tab JLPT Exam!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(examSubmissions) { sub ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (sub.passed) SageGreen.copy(alpha = 0.15f) else LacquerRed.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (sub.passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                contentDescription = null,
                                tint = if (sub.passed) SageGreen else LacquerRed
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = sub.examTitle,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Skor: ${sub.totalScore} / ${sub.totalMaxScore} Poin",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (sub.passed) SageGreen else LacquerRed
                        ) {
                            Text(
                                text = if (sub.passed) "LULUS" else "GAGAL",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}

@Composable
private fun SectionProgressItem(
    name: String,
    progress: Float,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
            Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = color)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}
