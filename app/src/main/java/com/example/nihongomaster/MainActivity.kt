package com.example.nihongomaster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.ui.components.*
import com.example.nihongomaster.ui.screens.*
import com.example.nihongomaster.ui.theme.NihongoMasterTheme

sealed class Screen(val route: String, val title: String, val icon: ImageVector, val selectedIcon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Outlined.Home, Icons.Filled.Home)
    object Grammar : Screen("grammar", "Grammar", Icons.Outlined.MenuBook, Icons.Filled.MenuBook)
    object Vocab : Screen("vocab", "Vocab", Icons.Outlined.Translate, Icons.Filled.Translate)
    object Kanji : Screen("kanji", "Kanji", Icons.Outlined.Edit, Icons.Filled.Edit)
    object Flashcards : Screen("flashcards", "SRS", Icons.Outlined.Style, Icons.Filled.Style)
    object Exam : Screen("exam", "Exam", Icons.Outlined.Timer, Icons.Filled.Timer)
    object Analytics : Screen("analytics", "Stats", Icons.Outlined.BarChart, Icons.Filled.BarChart)
}

class MainActivity : ComponentActivity() {
    private lateinit var repository: NihongoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        repository = NihongoRepository(applicationContext)

        setContent {
            NihongoMasterTheme {
                val currentLevel by repository.selectedLevel.collectAsState()
                val furiganaMode by repository.furiganaMode.collectAsState()
                val translationLang by repository.translationLang.collectAsState()
                val activeTextbook by repository.activeTextbook.collectAsState()
                val streakData by repository.streakData.collectAsState()

                var currentRoute by remember { mutableStateOf(Screen.Home.route) }

                // Dialog states
                var showAISenseiDialog by remember { mutableStateOf(false) }
                var aiSenseiTopic by remember { mutableStateOf<String?>(null) }
                var showStreakDialog by remember { mutableStateOf(false) }
                var showTextbookDialog by remember { mutableStateOf(false) }
                var showAboutDialog by remember { mutableStateOf(false) }

                val bottomNavItems = listOf(
                    Screen.Home,
                    Screen.Grammar,
                    Screen.Vocab,
                    Screen.Kanji,
                    Screen.Flashcards,
                    Screen.Exam,
                    Screen.Analytics
                )

                Scaffold(
                    topBar = {
                        AppTopBar(
                            currentLevel = currentLevel,
                            onSelectLevel = { repository.setLevel(it) },
                            furiganaMode = furiganaMode,
                            onToggleFurigana = { repository.setFuriganaMode(it) },
                            translationLang = translationLang,
                            onToggleLanguage = { repository.setTranslationLang(it) },
                            streakData = streakData,
                            onOpenStreakModal = { showStreakDialog = true },
                            onOpenAISensei = {
                                aiSenseiTopic = null
                                showAISenseiDialog = true
                            },
                            onOpenTextbookSelector = { showTextbookDialog = true },
                            onOpenAbout = { showAboutDialog = true }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            bottomNavItems.forEach { screen ->
                                val isSelected = currentRoute == screen.route
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) screen.selectedIcon else screen.icon,
                                            contentDescription = screen.title
                                        )
                                    },
                                    label = { Text(screen.title) },
                                    selected = isSelected,
                                    onClick = { currentRoute = screen.route },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentRoute) {
                            Screen.Home.route -> HomeScreen(
                                repository = repository,
                                onNavigateTab = { route -> currentRoute = route },
                                onOpenAISensei = { topic ->
                                    aiSenseiTopic = topic
                                    showAISenseiDialog = true
                                },
                                onStartExam = {
                                    currentRoute = Screen.Exam.route
                                }
                            )
                            Screen.Grammar.route -> GrammarScreen(
                                repository = repository,
                                onOpenAISensei = { topic ->
                                    aiSenseiTopic = topic
                                    showAISenseiDialog = true
                                }
                            )
                            Screen.Vocab.route -> VocabScreen(
                                repository = repository
                            )
                            Screen.Kanji.route -> KanjiScreen(
                                repository = repository
                            )
                            Screen.Flashcards.route -> FlashcardScreen(
                                repository = repository
                            )
                            Screen.Exam.route -> ExamScreen(
                                repository = repository
                            )
                            Screen.Analytics.route -> AnalyticsScreen(
                                repository = repository
                            )
                        }
                    }
                }

                // Modals & Dialogs
                if (showAISenseiDialog) {
                    AISenseiDialog(
                        repository = repository,
                        initialTopic = aiSenseiTopic,
                        onDismiss = { showAISenseiDialog = false }
                    )
                }

                if (showStreakDialog) {
                    DailyStreakDialog(
                        streakData = streakData,
                        onDismiss = { showStreakDialog = false }
                    )
                }

                if (showTextbookDialog) {
                    TextbookSelectorDialog(
                        selectedTextbook = activeTextbook,
                        onSelectTextbook = { repository.setTextbook(it) },
                        onDismiss = { showTextbookDialog = false }
                    )
                }

                if (showAboutDialog) {
                    AboutManifestoDialog(
                        onDismiss = { showAboutDialog = false }
                    )
                }
            }
        }
    }
}
