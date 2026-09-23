package com.example.nihongomaster.data

import android.content.Context
import android.content.SharedPreferences
import com.example.nihongomaster.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

class NihongoRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("nihongo_master_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    // State Flows
    private val _selectedLevel = MutableStateFlow(loadSelectedLevel())
    val selectedLevel: StateFlow<JLPTLevel> = _selectedLevel.asStateFlow()

    private val _furiganaMode = MutableStateFlow(loadFuriganaMode())
    val furiganaMode: StateFlow<FuriganaMode> = _furiganaMode.asStateFlow()

    private val _translationLang = MutableStateFlow(loadTranslationLang())
    val translationLang: StateFlow<TranslationLang> = _translationLang.asStateFlow()

    private val _activeTextbook = MutableStateFlow(loadTextbook())
    val activeTextbook: StateFlow<TextbookSource> = _activeTextbook.asStateFlow()

    private val _streakData = MutableStateFlow(loadStreak())
    val streakData: StateFlow<DailyStreakData> = _streakData.asStateFlow()

    private val _srsDeck = MutableStateFlow(loadSRSItems())
    val srsDeck: StateFlow<List<SRSItem>> = _srsDeck.asStateFlow()

    private val _bookmarkedGrammarIds = MutableStateFlow(loadBookmarkedIds("bookmarks_grammar"))
    val bookmarkedGrammarIds: StateFlow<Set<String>> = _bookmarkedGrammarIds.asStateFlow()

    private val _bookmarkedVocabIds = MutableStateFlow(loadBookmarkedIds("bookmarks_vocab"))
    val bookmarkedVocabIds: StateFlow<Set<String>> = _bookmarkedVocabIds.asStateFlow()

    private val _bookmarkedKanjiIds = MutableStateFlow(loadBookmarkedIds("bookmarks_kanji"))
    val bookmarkedKanjiIds: StateFlow<Set<String>> = _bookmarkedKanjiIds.asStateFlow()

    private val _masteredVocabIds = MutableStateFlow(loadBookmarkedIds("mastered_vocab"))
    val masteredVocabIds: StateFlow<Set<String>> = _masteredVocabIds.asStateFlow()

    private val _examSubmissions = MutableStateFlow(loadExamSubmissions())
    val examSubmissions: StateFlow<List<UserExamSubmission>> = _examSubmissions.asStateFlow()

    init {
        recordDailyStudy()
    }

    // Level Management
    fun setLevel(level: JLPTLevel) {
        _selectedLevel.value = level
        prefs.edit().putString("selected_level", level.name).apply()
    }

    private fun loadSelectedLevel(): JLPTLevel {
        val name = prefs.getString("selected_level", JLPTLevel.N3.name) ?: JLPTLevel.N3.name
        return try { JLPTLevel.valueOf(name) } catch (e: Exception) { JLPTLevel.N3 }
    }

    // Furigana Mode
    fun setFuriganaMode(mode: FuriganaMode) {
        _furiganaMode.value = mode
        prefs.edit().putString("furigana_mode", mode.name).apply()
    }

    private fun loadFuriganaMode(): FuriganaMode {
        val name = prefs.getString("furigana_mode", FuriganaMode.ALWAYS.name) ?: FuriganaMode.ALWAYS.name
        return try { FuriganaMode.valueOf(name) } catch (e: Exception) { FuriganaMode.ALWAYS }
    }

    // Translation Language
    fun setTranslationLang(lang: TranslationLang) {
        _translationLang.value = lang
        prefs.edit().putString("translation_lang", lang.name).apply()
    }

    private fun loadTranslationLang(): TranslationLang {
        val name = prefs.getString("translation_lang", TranslationLang.ID.name) ?: TranslationLang.ID.name
        return try { TranslationLang.valueOf(name) } catch (e: Exception) { TranslationLang.ID }
    }

    // Textbook Selection
    fun setTextbook(textbook: TextbookSource) {
        _activeTextbook.value = textbook
        prefs.edit().putString("active_textbook", textbook.name).apply()
    }

    private fun loadTextbook(): TextbookSource {
        val name = prefs.getString("active_textbook", TextbookSource.KANZEN_MASTER.name) ?: TextbookSource.KANZEN_MASTER.name
        return try { TextbookSource.valueOf(name) } catch (e: Exception) { TextbookSource.KANZEN_MASTER }
    }

    // Streak System
    private fun loadStreak(): DailyStreakData {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val lastDate = prefs.getString("streak_last_date", "") ?: ""
        val current = prefs.getInt("streak_current", 1)
        val longest = prefs.getInt("streak_longest", 1)
        val activeDates = prefs.getStringSet("streak_active_dates", emptySet())?.toList() ?: listOf(today)
        return DailyStreakData(
            currentStreak = if (lastDate == today) current else current,
            longestStreak = longest,
            lastActiveDate = lastDate,
            activeDates = activeDates,
            todayStudied = (lastDate == today)
        )
    }

    fun recordDailyStudy() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentStreak = prefs.getInt("streak_current", 0)
        val longestStreak = prefs.getInt("streak_longest", 0)
        val lastDate = prefs.getString("streak_last_date", "") ?: ""
        val dates = (prefs.getStringSet("streak_active_dates", emptySet()) ?: emptySet()).toMutableSet()
        dates.add(today)

        val newCurrent = if (lastDate == today) {
            maxOf(currentStreak, 1)
        } else {
            currentStreak + 1
        }
        val newLongest = maxOf(longestStreak, newCurrent)

        prefs.edit()
            .putString("streak_last_date", today)
            .putInt("streak_current", newCurrent)
            .putInt("streak_longest", newLongest)
            .putStringSet("streak_active_dates", dates)
            .apply()

        _streakData.value = DailyStreakData(
            currentStreak = newCurrent,
            longestStreak = newLongest,
            lastActiveDate = today,
            activeDates = dates.toList(),
            todayStudied = true
        )
    }

    // Bookmarks & Mastery
    fun toggleBookmarkGrammar(id: String) {
        val set = _bookmarkedGrammarIds.value.toMutableSet()
        if (set.contains(id)) set.remove(id) else set.add(id)
        _bookmarkedGrammarIds.value = set
        prefs.edit().putStringSet("bookmarks_grammar", set).apply()
    }

    fun toggleBookmarkVocab(id: String) {
        val set = _bookmarkedVocabIds.value.toMutableSet()
        if (set.contains(id)) set.remove(id) else set.add(id)
        _bookmarkedVocabIds.value = set
        prefs.edit().putStringSet("bookmarks_vocab", set).apply()
    }

    fun toggleMasteredVocab(id: String) {
        val set = _masteredVocabIds.value.toMutableSet()
        if (set.contains(id)) set.remove(id) else set.add(id)
        _masteredVocabIds.value = set
        prefs.edit().putStringSet("mastered_vocab", set).apply()
    }

    fun toggleBookmarkKanji(id: String) {
        val set = _bookmarkedKanjiIds.value.toMutableSet()
        if (set.contains(id)) set.remove(id) else set.add(id)
        _bookmarkedKanjiIds.value = set
        prefs.edit().putStringSet("bookmarks_kanji", set).apply()
    }

    private fun loadBookmarkedIds(key: String): Set<String> {
        return prefs.getStringSet(key, emptySet()) ?: emptySet()
    }

    // SRS Spaced Repetition Logic (SM-2 Algorithm)
    private fun loadSRSItems(): List<SRSItem> {
        val raw = prefs.getString("srs_deck_json", null)
        if (!raw.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<SRSItem>>(raw)
            } catch (e: Exception) {
                // fallback to initial deck
            }
        }
        return InitialData.getInitialSRSDeck()
    }

    private fun saveSRSItems(deck: List<SRSItem>) {
        _srsDeck.value = deck
        val encoded = json.encodeToString(deck)
        prefs.edit().putString("srs_deck_json", encoded).apply()
    }

    fun addOrUpdateSRSItem(item: SRSItem) {
        val list = _srsDeck.value.toMutableList()
        val index = list.indexOfFirst { it.id == item.id }
        if (index >= 0) {
            list[index] = item
        } else {
            list.add(item)
        }
        saveSRSItems(list)
    }

    fun answerSRSItem(item: SRSItem, rating: SRSRating) {
        val now = System.currentTimeMillis()
        var repetitions = item.repetitions
        var easeFactor = item.easeFactor
        var intervalDays = item.intervalDays

        when (rating) {
            SRSRating.AGAIN -> {
                repetitions = 0
                intervalDays = 1
                easeFactor = maxOf(1.3, easeFactor - 0.2)
            }
            SRSRating.HARD -> {
                intervalDays = maxOf(1, (intervalDays * 1.2).toInt())
                easeFactor = maxOf(1.3, easeFactor - 0.15)
            }
            SRSRating.GOOD -> {
                repetitions += 1
                intervalDays = if (repetitions == 1) 1 else if (repetitions == 2) 3 else (intervalDays * easeFactor).toInt()
            }
            SRSRating.EASY -> {
                repetitions += 1
                intervalDays = if (repetitions == 1) 2 else if (repetitions == 2) 5 else (intervalDays * easeFactor * 1.3).toInt()
                easeFactor += 0.15
            }
        }

        val dueDate = now + (intervalDays.toLong() * 24 * 60 * 60 * 1000)
        val newState = if (repetitions >= 4) SRSState.MASTERED else if (repetitions > 0) SRSState.REVIEW else SRSState.LEARNING

        val updated = item.copy(
            repetitions = repetitions,
            intervalDays = intervalDays,
            easeFactor = easeFactor,
            dueDate = dueDate,
            lastReviewed = now,
            state = newState
        )
        addOrUpdateSRSItem(updated)
        recordDailyStudy()
    }

    // Exam Submissions
    private fun loadExamSubmissions(): List<UserExamSubmission> {
        val raw = prefs.getString("exam_submissions_json", null)
        if (!raw.isNullOrBlank()) {
            try {
                return json.decodeFromString<List<UserExamSubmission>>(raw)
            } catch (e: Exception) {
                // ignore
            }
        }
        return emptyList()
    }

    fun saveExamSubmission(submission: UserExamSubmission) {
        val list = _examSubmissions.value.toMutableList()
        list.add(0, submission)
        _examSubmissions.value = list
        prefs.edit().putString("exam_submissions_json", json.encodeToString(list)).apply()
        recordDailyStudy()
    }

    // AI Sensei Q&A logic with smart simulated and contextual responses
    fun askAISensei(prompt: String, contextTopic: String?): String {
        val clean = prompt.trim().lowercase()
        return when {
            clean.contains("wake ni wa ikanai") || clean.contains("わけにはいかない") ->
                "【〜わけにはいかない (Wake ni wa ikanai)】\n" +
                "• **Arti:** Tidak bisa / tidak boleh (karena alasan moral, sosial, atau etika tanggung jawab).\n" +
                "• **Perbedaan dengan 〜できない (Dekinai):**\n" +
                "  - *Dekinai* menyatakan ketidakmampuan fisik atau kemampuan objektif (e.g., tidak bisa berenang).\n" +
                "  - *Wake ni wa ikanai* secara fisik bisa dilakukan, tapi terhalang rasa tanggung jawab, norma, atau moralitas.\n" +
                "• **Contoh:** 明日試験があるから、今日遊ぶわけにはいかない (Karena besok ada ujian, saya tidak bisa main hari ini)."

            clean.contains("koto ga aru") || clean.contains("ことがある") ->
                "【〜ことがある (Koto ga aru)】\n" +
                "1. **Bentuk Lampau (V-Ta + ことがある):** Menyatakan *pengalaman masa lalu* (Pernah...).\n" +
                "   • Contoh: 日本へ行ったことがある (Saya pernah pergi ke Jepang).\n" +
                "2. **Bentuk Kamus (V-Jisho + ことがある):** Menyatakan *kebiasaan situasional* (Kadang-kadang...).\n" +
                "   • Contoh: 朝ご飯を食べないことがある (Kadang-kadang saya tidak sarapan)."

            clean.contains("tips") || clean.contains("lulus") || clean.contains("jlpt") ->
                "Sensei punya 3 tips utama untuk lulus JLPT:\n" +
                "1. **Kuasai Dokkai (Bacaan):** Baca soal pertanyaannya dulu sebelum membaca teks panjang agar tahu fokus informasinya.\n" +
                "2. **Star Questions (★):** Gabungkan partikel dengan kata benda di depannya terlebih dahulu, baru susun predikat akhir.\n" +
                "3. **Gunakan SRS Flashcards:** Ulangi kosakata dan kanji setiap hari selama 10-15 menit untuk memori jangka panjang."

            clean.contains("star") || clean.contains("bintang") || clean.contains("susun") ->
                "Untuk soal Bintang (★ Sentence Reordering):\n" +
                "1. Temukan kata sebelum titik-titik pertama dan setelah titik-titik terakhir.\n" +
                "2. Pasangkan 4 opsi menjadi 2 pasang frasa logis.\n" +
                "3. Cari tahu posisi bintang (biasanya di slot ke-3) dan masukkan opsi yang tepat!"

            else ->
                "Hai! Sensei siap membantumu belajar bahasa Jepang. Kamu bisa menanyakan:\n" +
                "• Perbedaan nuansa pola tata bahasa (Bunpou)\n" +
                "• Arti dan cara baca Kanji & Jukugo\n" +
                "• Penjelasan soal ujian JLPT N5 - N2\n" +
                "• Contoh kalimat percakapan sehari-hari"
        }
    }
}
