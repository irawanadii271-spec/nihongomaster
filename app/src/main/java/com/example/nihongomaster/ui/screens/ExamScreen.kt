package com.example.nihongomaster.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nihongomaster.data.InitialData
import com.example.nihongomaster.data.NihongoRepository
import com.example.nihongomaster.model.*
import com.example.nihongomaster.ui.components.RubyText
import com.example.nihongomaster.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ExamScreen(
    repository: NihongoRepository
) {
    val currentLevel by repository.selectedLevel.collectAsState()
    val furiganaMode by repository.furiganaMode.collectAsState()
    val translationLang by repository.translationLang.collectAsState()

    var activeExam by remember { mutableStateOf<ExamPaper?>(null) }
    var currentSectionIndex by remember { mutableStateOf(0) }
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf<Map<String, Int>>(emptyMap()) } // questionId -> selectedIndex
    var examSubmissionResult by remember { mutableStateOf<UserExamSubmission?>(null) }
    var remainingSeconds by remember { mutableStateOf(105 * 60) }
    var isTimerRunning by remember { mutableStateOf(false) }

    // Countdown timer
    LaunchedEffect(isTimerRunning, remainingSeconds) {
        if (isTimerRunning && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        }
    }

    val availableExams = remember(currentLevel) {
        InitialData.EXAM_PAPERS.filter { it.level == currentLevel }
    }

    fun finishExam() {
        val exam = activeExam ?: return
        isTimerRunning = false

        var totalScore = 0
        val correctCounts = mutableMapOf<String, Int>()

        exam.sections.forEach { sec ->
            var secCorrect = 0
            sec.questions.forEach { q ->
                val selected = userAnswers[q.id]
                if (selected == q.correctAnswer) {
                    totalScore += q.points
                    secCorrect += 1
                }
            }
            correctCounts[sec.type] = secCorrect
        }

        val passed = totalScore >= exam.passingScore
        val submission = UserExamSubmission(
            examId = exam.id,
            examTitle = exam.title,
            level = exam.level,
            totalScore = totalScore,
            totalMaxScore = exam.totalMaxScore,
            passed = passed,
            failedReason = if (!passed) "Skor total belum mencapai batas kelulusan ${exam.passingScore} poin." else "",
            answers = userAnswers,
            correctCounts = correctCounts
        )
        repository.saveExamSubmission(submission)
        examSubmissionResult = submission
    }

    if (examSubmissionResult != null) {
        // Exam Result Screen
        ExamResultView(
            submission = examSubmissionResult!!,
            exam = activeExam!!,
            translationLang = translationLang,
            furiganaMode = furiganaMode,
            onRestart = {
                examSubmissionResult = null
                activeExam = null
                userAnswers = emptyMap()
            }
        )
    } else if (activeExam != null) {
        // Active Timed Exam Session
        val exam = activeExam!!
        val currentSection = exam.sections.getOrNull(currentSectionIndex) ?: exam.sections.first()
        val allQuestions = currentSection.questions
        val currentQuestion = allQuestions.getOrNull(currentQuestionIndex) ?: allQuestions.first()

        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        val timerFormatted = String.format("%02d:%02d", minutes, seconds)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Exam Header Bar with Timer & Exit
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Countdown Timer Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (remainingSeconds < 300) LacquerRed.copy(alpha = 0.15f) else IndigoSlate.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Timer",
                            tint = if (remainingSeconds < 300) LacquerRed else IndigoSlate,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = timerFormatted,
                            fontWeight = FontWeight.Bold,
                            color = if (remainingSeconds < 300) LacquerRed else IndigoSlate,
                            fontSize = 14.sp
                        )
                    }
                }

                Button(
                    onClick = { finishExam() },
                    colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text("Selesaikan Ujian", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Section Selector Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                exam.sections.forEachIndexed { sIdx, sec ->
                    val isSelected = sIdx == currentSectionIndex
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) LacquerRed else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                currentSectionIndex = sIdx
                                currentQuestionIndex = 0
                            }
                    ) {
                        Text(
                            text = sec.title.split(" ").first(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }
            }

            // Question Progress Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentSection.title} • Soal ${currentQuestionIndex + 1} dari ${allQuestions.size}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${currentQuestion.points} Poin",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Question Content Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Instruction
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = currentQuestion.instruction,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            if (currentQuestion.instructionId.isNotBlank() && translationLang != TranslationLang.EN) {
                                Text(
                                    text = currentQuestion.instructionId,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Dokkai Reading Comprehension Passage
                    if (currentQuestion.passageData != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = OchreAmber.copy(alpha = 0.08f),
                            border = ButtonDefaults.outlinedButtonBorder,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "📖 ${currentQuestion.passageData.title}",
                                    fontWeight = FontWeight.Bold,
                                    color = OchreAmber,
                                    style = MaterialTheme.typography.titleSmall
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentQuestion.passageData.passage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }

                    // Star Question Interactive Builder Visualization (★)
                    if (currentQuestion.starData != null) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = IndigoSlate.copy(alpha = 0.1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "★ Soal Susunan Bintang:",
                                    fontWeight = FontWeight.Bold,
                                    color = IndigoSlate,
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${currentQuestion.starData.preText} [ 1 ] [ 2 ] [ ★ ] [ 4 ] ${currentQuestion.starData.postText}",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    // Question Sentence
                    RubyText(
                        japanese = currentQuestion.question,
                        reading = currentQuestion.questionRuby,
                        furiganaMode = furiganaMode,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Options List
                    val selectedOption = userAnswers[currentQuestion.id]
                    currentQuestion.options.forEachIndexed { optIdx, optText ->
                        val isSelected = selectedOption == optIdx
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) LacquerRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (isSelected) ButtonDefaults.outlinedButtonBorder else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    val updated = userAnswers.toMutableMap()
                                    updated[currentQuestion.id] = optIdx
                                    userAnswers = updated
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) LacquerRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${optIdx + 1}",
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = optText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Next / Prev
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex -= 1
                        } else if (currentSectionIndex > 0) {
                            currentSectionIndex -= 1
                            currentQuestionIndex = exam.sections[currentSectionIndex].questions.size - 1
                        }
                    },
                    enabled = currentSectionIndex > 0 || currentQuestionIndex > 0,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sebelumnya")
                }

                Button(
                    onClick = {
                        if (currentQuestionIndex < allQuestions.size - 1) {
                            currentQuestionIndex += 1
                        } else if (currentSectionIndex < exam.sections.size - 1) {
                            currentSectionIndex += 1
                            currentQuestionIndex = 0
                        } else {
                            finishExam()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        if (currentSectionIndex == exam.sections.size - 1 && currentQuestionIndex == allQuestions.size - 1)
                            "Kirim Jawaban"
                        else "Berikutnya"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    } else {
        // Exam Selection List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Simulasi Ujian Resmi JLPT (${currentLevel.displayName})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Format waktu realistis dengan pembagian sub-sesi Kosakata, Tata Bahasa & Bacaan.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(availableExams) { exam ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = IndigoSlate.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${exam.level.displayName} • ${exam.year} ${exam.session}",
                                        color = IndigoSlate,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Text(
                                    text = "Durasi: ${exam.totalTimeMinutes} Menit",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = exam.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = exam.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Skor Lulus: ${exam.passingScore} / ${exam.totalMaxScore} Poin",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SageGreen
                                )

                                Button(
                                    onClick = {
                                        activeExam = exam
                                        currentSectionIndex = 0
                                        currentQuestionIndex = 0
                                        userAnswers = emptyMap()
                                        remainingSeconds = exam.totalTimeMinutes * 60
                                        isTimerRunning = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Mulai Ujian", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExamResultView(
    submission: UserExamSubmission,
    exam: ExamPaper,
    translationLang: TranslationLang,
    furiganaMode: FuriganaMode,
    onRestart: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Result Banner
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = if (submission.passed) SageGreen.copy(alpha = 0.15f) else LacquerRed.copy(alpha = 0.15f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (submission.passed) "🎉 LULUS (合格)" else "BELUM LULUS (不合格)",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (submission.passed) SageGreen else LacquerRed
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Skor Akhir: ${submission.totalScore} / ${submission.totalMaxScore} Poin",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Standar kelulusan ${exam.level.displayName}: ${exam.passingScore} Poin",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Explanations Review
        Text(
            text = "Pembahasan Jawaban Lengkap",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        exam.sections.forEach { sec ->
            sec.questions.forEachIndexed { qIdx, q ->
                val selected = submission.answers[q.id]
                val isCorrect = selected == q.correctAnswer

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Soal #${qIdx + 1} (${q.subCategory})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (isCorrect) "✓ Benar (+${q.points})" else "✗ Salah (0)",
                                color = if (isCorrect) SageGreen else LacquerRed,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        RubyText(
                            japanese = q.question,
                            reading = q.questionRuby,
                            furiganaMode = furiganaMode,
                            fontSize = 15.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Jawaban Benar: ${q.correctAnswer + 1}. ${q.options.getOrNull(q.correctAnswer) ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = SageGreen
                        )

                        if (selected != null && selected != q.correctAnswer) {
                            Text(
                                text = "Jawaban Kamu: ${selected + 1}. ${q.options.getOrNull(selected) ?: ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = LacquerRed
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        ) {
                            Text(
                                text = "💡 Pembahasan: ${if (translationLang == TranslationLang.EN) q.explanationEn else q.explanationId}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = LacquerRed),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Kembali ke Menu Ujian", fontWeight = FontWeight.Bold)
        }
    }
}
