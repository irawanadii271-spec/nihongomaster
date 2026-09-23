package com.example.nihongomaster.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.nihongomaster.model.DailyStreakData
import com.example.nihongomaster.model.FuriganaMode
import com.example.nihongomaster.model.JLPTLevel
import com.example.nihongomaster.model.TranslationLang
import com.example.nihongomaster.ui.theme.LacquerRed
import com.example.nihongomaster.ui.theme.OchreAmber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentLevel: JLPTLevel,
    onSelectLevel: (JLPTLevel) -> Unit,
    furiganaMode: FuriganaMode,
    onToggleFurigana: (FuriganaMode) -> Unit,
    translationLang: TranslationLang,
    onToggleLanguage: (TranslationLang) -> Unit,
    streakData: DailyStreakData,
    onOpenStreakModal: () -> Unit,
    onOpenAISensei: () -> Unit,
    onOpenTextbookSelector: () -> Unit,
    onOpenAbout: () -> Unit
) {
    var showLevelMenu by remember { mutableStateOf(false) }
    var showFuriganaMenu by remember { mutableStateOf(false) }
    var showLangMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(LacquerRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "日",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Column {
                            Text(
                                text = "NihongoMaster",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "JLPT Study Suite",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    // Daily Streak Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(OchreAmber.copy(alpha = 0.15f))
                            .clickable { onOpenStreakModal() }
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = OchreAmber,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${streakData.currentStreak} Hari",
                            style = MaterialTheme.typography.labelLarge,
                            color = OchreAmber,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // AI Sensei Button
                    IconButton(
                        onClick = onOpenAISensei,
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(LacquerRed.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Sensei",
                            tint = LacquerRed
                        )
                    }

                    // More Menu / About
                    IconButton(onClick = onOpenAbout) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Info & Manifesto"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )

            // Quick Filters Bar: JLPT Level Pill, Furigana Toggle, Language Switcher, Textbook Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level Selector Dropdown Pill
                Box {
                    AssistChip(
                        onClick = { showLevelMenu = true },
                        label = { Text(currentLevel.displayName, fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.School,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = LacquerRed
                            )
                        },
                        trailingIcon = {
                            Icon(
                                Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = LacquerRed.copy(alpha = 0.08f),
                            labelColor = LacquerRed
                        )
                    )

                    DropdownMenu(
                        expanded = showLevelMenu,
                        onDismissRequest = { showLevelMenu = false }
                    ) {
                        listOf(JLPTLevel.N5, JLPTLevel.N4, JLPTLevel.N3, JLPTLevel.N2).forEach { lvl ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        lvl.displayName,
                                        fontWeight = if (lvl == currentLevel) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    onSelectLevel(lvl)
                                    showLevelMenu = false
                                },
                                leadingIcon = if (lvl == currentLevel) {
                                    { Icon(Icons.Default.Check, contentDescription = null, tint = LacquerRed) }
                                } else null
                            )
                        }
                    }
                }

                // Furigana Mode Toggle
                Box {
                    AssistChip(
                        onClick = { showFuriganaMenu = true },
                        label = {
                            Text(
                                when (furiganaMode) {
                                    FuriganaMode.ALWAYS -> "Furigana: On"
                                    FuriganaMode.TAP_TO_REVEAL -> "Furigana: Tap"
                                    FuriganaMode.OFF -> "Furigana: Off"
                                },
                                fontSize = 12.sp
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )

                    DropdownMenu(
                        expanded = showFuriganaMenu,
                        onDismissRequest = { showFuriganaMenu = false }
                    ) {
                        FuriganaMode.values().forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.label) },
                                onClick = {
                                    onToggleFurigana(mode)
                                    showFuriganaMenu = false
                                },
                                leadingIcon = if (mode == furiganaMode) {
                                    { Icon(Icons.Default.Check, contentDescription = null) }
                                } else null
                            )
                        }
                    }
                }

                // Language Switcher
                Box {
                    AssistChip(
                        onClick = { showLangMenu = true },
                        label = { Text("Lang: ${translationLang.name}", fontSize = 12.sp) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    )

                    DropdownMenu(
                        expanded = showLangMenu,
                        onDismissRequest = { showLangMenu = false }
                    ) {
                        TranslationLang.values().forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.label) },
                                onClick = {
                                    onToggleLanguage(lang)
                                    showLangMenu = false
                                },
                                leadingIcon = if (lang == translationLang) {
                                    { Icon(Icons.Default.Check, contentDescription = null) }
                                } else null
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Textbook Selector Button
                IconButton(
                    onClick = onOpenTextbookSelector,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = "Pilih Buku Kurikulum",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
