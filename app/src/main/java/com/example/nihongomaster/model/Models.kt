package com.example.nihongomaster.model

import kotlinx.serialization.Serializable

@Serializable
enum class JLPTLevel(val displayName: String) {
    N5("JLPT N5"),
    N4("JLPT N4"),
    N3("JLPT N3"),
    N2("JLPT N2"),
    N1("JLPT N1")
}

@Serializable
enum class FuriganaMode(val label: String) {
    ALWAYS("Furigana: Always"),
    TAP_TO_REVEAL("Furigana: Tap"),
    OFF("Furigana: Off")
}

@Serializable
enum class TranslationLang(val label: String) {
    ID("Indonesia"),
    EN("English"),
    BOTH("ID & EN")
}

@Serializable
enum class TextbookSource(val label: String, val level: String) {
    KANZEN_MASTER("Shin Kanzen Master", "N2/N3"),
    SOU_MATOME("Nihongo Sou Matome", "N3/N2"),
    MINNA_NO_NIHONGO("Minna no Nihongo", "N5/N4"),
    NIHONGO_NO_MORI("Nihongo no Mori", "N3"),
    TRY_N2("TRY! Japanese", "N2"),
    ALL_IN_ONE("Comprehensive Guide", "All")
}

@Serializable
data class ExampleSentence(
    val japanese: String,
    val reading: String = "",
    val english: String = "",
    val indonesian: String = ""
)

@Serializable
data class GrammarPoint(
    val id: String,
    val bunpouNumber: Int = 0,
    val chapterNumber: Int = 1,
    val chapterName: String = "",
    val title: String,
    val meaningEn: String,
    val meaningId: String,
    val level: JLPTLevel,
    val category: String,
    val structure: List<String> = emptyList(),
    val explanationEn: String = "",
    val explanationId: String = "",
    val nuanceNotes: String = "",
    val cautionNotes: String = "",
    val examples: List<ExampleSentence> = emptyList(),
    val relatedGrammar: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val isBookmarked: Boolean = false
)

@Serializable
data class VocabWord(
    val id: String,
    val word: String,
    val reading: String,
    val romaji: String = "",
    val meaningEn: String,
    val meaningId: String,
    val level: JLPTLevel,
    val partOfSpeech: String,
    val category: String,
    val pitchAccent: String = "",
    val example: ExampleSentence = ExampleSentence(""),
    val antonyms: List<String> = emptyList(),
    val synonyms: List<String> = emptyList(),
    val isStarred: Boolean = false,
    val isMastered: Boolean = false
)

@Serializable
data class KanjiJukugo(
    val word: String,
    val reading: String,
    val meaningEn: String,
    val meaningId: String
)

@Serializable
data class KanjiItem(
    val id: String,
    val kanji: String,
    val onyomi: List<String> = emptyList(),
    val kunyomi: List<String> = emptyList(),
    val meaningEn: String,
    val meaningId: String,
    val level: JLPTLevel,
    val strokes: Int = 0,
    val radical: String = "",
    val radicalMeaning: String = "",
    val grade: Int = 0,
    val jukugo: List<KanjiJukugo> = emptyList(),
    val exampleSentence: ExampleSentence = ExampleSentence(""),
    val isBookmarked: Boolean = false,
    val isMastered: Boolean = false
)

@Serializable
enum class SRSRating {
    AGAIN, HARD, GOOD, EASY
}

@Serializable
enum class SRSState {
    NEW, LEARNING, REVIEW, MASTERED
}

@Serializable
data class SRSItem(
    val id: String,
    val cardType: String, // "kanji", "vocab", "grammar"
    val targetId: String,
    val frontText: String,
    val frontSubText: String = "",
    val backReading: String,
    val backMeaningEn: String,
    val backMeaningId: String,
    val exampleJapanese: String = "",
    val exampleReading: String = "",
    val exampleTranslation: String = "",
    val level: JLPTLevel = JLPTLevel.N3,
    val repetitions: Int = 0,
    val intervalDays: Int = 1,
    val easeFactor: Double = 2.5,
    val dueDate: Long = System.currentTimeMillis(),
    val lastReviewed: Long = 0L,
    val state: SRSState = SRSState.NEW
)

@Serializable
data class StarQuestionData(
    val preText: String = "",
    val postText: String = "",
    val starSlotIndex: Int = 2, // 0-indexed position (e.g. 2 means [1][2][★][4])
    val correctOrder: List<Int>, // 0-indexed options arrangement e.g. [2, 0, 1, 3]
    val options: List<String>
)

@Serializable
data class DokkaiPassageData(
    val id: String = "",
    val title: String = "",
    val passage: String,
    val passageRuby: String = "",
    val passageType: String = "medium", // "short", "medium", "long", "information_retrieval"
    val sourceNote: String = ""
)

@Serializable
data class ExamQuestion(
    val id: String,
    val level: JLPTLevel,
    val section: String, // "vocabulary", "grammar", "reading", "listening"
    val subCategory: String, // "Kanji Reading", "Contextual Usage", "Star Sentence", "Short Passage", etc.
    val instruction: String,
    val instructionId: String = "",
    val readingPassage: String = "",
    val passageTitle: String = "",
    val passageData: DokkaiPassageData? = null,
    val starData: StarQuestionData? = null,
    val question: String,
    val questionRuby: String = "",
    val options: List<String>,
    val correctAnswer: Int, // 0..3
    val explanationEn: String = "",
    val explanationId: String = "",
    val points: Int = 2
)

@Serializable
data class ExamSectionConfig(
    val type: String, // "vocabulary", "grammar", "reading", "listening"
    val title: String,
    val titleJp: String,
    val timeMinutes: Int,
    val questions: List<ExamQuestion>,
    val maxScore: Int = 60,
    val minPassScore: Int = 19
)

@Serializable
data class ExamPaper(
    val id: String,
    val title: String,
    val titleJp: String = "",
    val level: JLPTLevel,
    val year: String = "2024",
    val session: String = "December",
    val description: String,
    val sections: List<ExamSectionConfig>,
    val totalTimeMinutes: Int = 105,
    val totalMaxScore: Int = 180,
    val passingScore: Int = 95
)

@Serializable
data class UserExamSubmission(
    val examId: String,
    val examTitle: String,
    val level: JLPTLevel,
    val timestamp: Long = System.currentTimeMillis(),
    val totalScore: Int,
    val totalMaxScore: Int,
    val passed: Boolean,
    val failedReason: String = "",
    val answers: Map<String, Int> = emptyMap(), // questionId -> selectedAnswer
    val correctCounts: Map<String, Int> = emptyMap() // section -> correctCount
)

@Serializable
data class DailyStreakData(
    val currentStreak: Int = 1,
    val longestStreak: Int = 1,
    val lastActiveDate: String = "",
    val activeDates: List<String> = emptyList(),
    val todayStudied: Boolean = true
)

@Serializable
data class YouTubeVideo(
    val id: String,
    val title: String,
    val channel: String,
    val duration: String,
    val level: JLPTLevel,
    val category: String,
    val youtubeId: String,
    val description: String
)

@Serializable
data class ReleaseNoteItem(
    val version: String,
    val date: String,
    val title: String,
    val highlights: List<String>
)
