package com.example.nihongomaster.data

import com.example.nihongomaster.model.*

object InitialData {

    // --- GRAMMAR DATA ---
    val GRAMMAR_POINTS: List<GrammarPoint> = listOf(
        // N3 Grammar Points
        GrammarPoint(
            id = "n3-g-01",
            bunpouNumber = 1,
            chapterNumber = 1,
            chapterName = "Alasan & Penyebab",
            title = "〜せいで / 〜せいか",
            meaningEn = "Because of / Due to (negative outcome)",
            meaningId = "Gara-gara / Karena (akibat buruk / menyalahkan sesuatu)",
            level = JLPTLevel.N3,
            category = "Alasan & Penyebab",
            structure = listOf("V (bentuk biasa) + せいで", "いAdj / なAdj (+な) + せいで", "N + の + せいで"),
            explanationEn = "Used when blaming someone or something for an unfortunate result. 〜せいか indicates uncertainty: 'Perhaps because of...'",
            explanationId = "Digunakan saat menyatakan alasan atau kambing hitam terjadinya suatu hal yang buruk/negatif. Jika menggunakan せいか artinya 'mungkin gara-gara...'.",
            nuanceNotes = "Hanya digunakan untuk hal negatif atau penyesalan. Untuk hal positif gunakan 〜おかげで (berkat).",
            cautionNotes = "Tidak boleh digunakan untuk menyatakan rasa terima kasih atau peristiwa menggembirakan.",
            examples = listOf(
                ExampleSentence("バスが遅れたせいで、授業に間に合わなかった。", "ばすがおくれたせいで、じゅぎょうにまにあわなかった。", "Because the bus was late, I didn't make it to class in time.", "Gara-gara bis terlambat, saya tidak sempat masuk kelas tepat waktu."),
                ExampleSentence("寝不足のせいか、頭が痛い。", "ねぶそくのせいか、あたまがいたい。", "Perhaps because of lack of sleep, my head hurts.", "Mungkin gara-gara kurang tidur, kepalaku sakit.")
            ),
            relatedGrammar = listOf("〜おかげで", "〜ために", "〜ばかりに"),
            tags = listOf("N3", "Kanzen Master", "Alasan", "Negatif")
        ),
        GrammarPoint(
            id = "n3-g-02",
            bunpouNumber = 2,
            chapterNumber = 1,
            chapterName = "Alasan & Penyebab",
            title = "〜おかげで / 〜おかげだ",
            meaningEn = "Thanks to / Because of (positive outcome)",
            meaningId = "Berkat / Berkat bantuan (hasil positif)",
            level = JLPTLevel.N3,
            category = "Alasan & Penyebab",
            structure = listOf("V (bentuk biasa) + おかげで", "いAdj / なAdj (+な) + おかげで", "N + の + おかげで"),
            explanationEn = "Used to express gratitude that a good result happened thanks to someone or something.",
            explanationId = "Digunakan untuk mengungkapkan rasa syukur atau terima kasih atas suatu bantuan/keadaan yang membawa hasil baik.",
            nuanceNotes = "Jika digunakan secara sarkasme, bisa bermakna sindiran.",
            cautionNotes = "Hampir selalu digunakan untuk situasi positif.",
            examples = listOf(
                ExampleSentence("先生のおかげで、N3に合格できました。", "せんせいのおかげで、えぬさんにうかりました。", "Thanks to the teacher, I was able to pass N3.", "Berkat Sensei, saya bisa lulus N3."),
                ExampleSentence("薬を飲んだおかげで、熱が下がりました。", "くすりをのんだおかげで、ねつがさがりました。", "Thanks to taking the medicine, my fever went down.", "Berkat minum obat, demam saya sudah turun.")
            ),
            relatedGrammar = listOf("〜せいで", "〜ために"),
            tags = listOf("N3", "Kanzen Master", "Alasan", "Positif")
        ),
        GrammarPoint(
            id = "n3-g-03",
            bunpouNumber = 3,
            chapterNumber = 2,
            chapterName = "Situasi & Perubahan",
            title = "〜わけにはいかない",
            meaningEn = "Cannot afford to / Must not (due to social/moral obligations)",
            meaningId = "Tidak bisa / Tidak boleh (karena tanggung jawab moral/sosial)",
            level = JLPTLevel.N3,
            category = "Kewajiban & Moral",
            structure = listOf("V (Kamus / Jisho) + わけにはいかない"),
            explanationEn = "Expresses that one cannot do something due to social norms, conscience, or severe consequences, even if physically capable.",
            explanationId = "Menyatakan bahwa pembicara tidak bisa melakukan suatu hal karena terhalang etika, norma sosial, atau rasa tanggung jawab moral.",
            nuanceNotes = "Bukan ketidakmampuan fisik, melainkan hambatan psikologis atau etika.",
            cautionNotes = "Bentuk negatifnya 〜ないわけにはいかない artinya 'harus / mau tidak mau harus dilakukan'.",
            examples = listOf(
                ExampleSentence("大事な会議があるから、休むわけにはいかない。", "だいじなかいぎがあるから、やすむわけにはいかない。", "Since there is an important meeting, I cannot afford to take a day off.", "Karena ada rapat penting, saya tidak bisa bolos kerja."),
                ExampleSentence("親友の頼みだから、断るわけにはいかない。", "しんゆうのたのみだから、ことわるわけにはいかない。", "Since it is my best friend's request, I cannot simply refuse.", "Karena ini permintaan sahabat terbaik, saya tidak bisa menolaknya.")
            ),
            relatedGrammar = listOf("〜ないわけにはいかない", "〜ざるを得ない"),
            tags = listOf("N3", "Kanzen Master", "Kewajiban")
        ),
        GrammarPoint(
            id = "n3-g-04",
            bunpouNumber = 4,
            chapterNumber = 3,
            chapterName = "Waktu & Momentum",
            title = "〜最中に / 〜最中だ",
            meaningEn = "In the middle of (just when an interruption happens)",
            meaningId = "Tepat di tengah-tengah / Sedang asyik-asyiknya",
            level = JLPTLevel.N3,
            category = "Waktu & Momentum",
            structure = listOf("V (ている) + 最中に", "N + の + 最中に"),
            explanationEn = "Indicates that right in the middle of performing an action, an unexpected event or interruption occurred.",
            explanationId = "Menyatakan tepat di puncak/tengah-tengah sedang melakukan suatu aktivitas, tiba-tiba terjadi gangguan yang tak terduga.",
            nuanceNotes = "Sering diikuti peristiwa yang mengganggu fokus (seperti mati lampu, gempa, telepon berdering).",
            cautionNotes = "Tidak dipakai untuk durasi waktu yang sangat panjang (seperti 'di tengah tahun').",
            examples = listOf(
                ExampleSentence("食事の最中に、地震が起きた。", "しょくじのさいちゅうに、じしんがおきた。", "Right in the middle of our meal, an earthquake happened.", "Tepat di tengah-tengah makan, gempa bumi terjadi."),
                ExampleSentence("試験の最中に、携帯が鳴ってしまった。", "しけんのさいちゅうに、けいたいがなってしまった。", "In the middle of the exam, the cell phone accidentally rang.", "Tepat di tengah-tengah ujian, ponsel berdering.")
            ),
            relatedGrammar = listOf("〜うちに", "〜あいだに"),
            tags = listOf("N3", "Kanzen Master", "Waktu")
        ),
        // N4 Grammar Points
        GrammarPoint(
            id = "n4-g-01",
            bunpouNumber = 1,
            chapterNumber = 1,
            chapterName = "Pengalaman & Kebiasaan",
            title = "〜たことがある",
            meaningEn = "Have had the experience of doing...",
            meaningId = "Pernah (memiliki pengalaman melakukan sesuatu)",
            level = JLPTLevel.N4,
            category = "Pengalaman",
            structure = listOf("V (bentuk Lampau / Ta-form) + ことがある"),
            explanationEn = "Used to state whether someone has experienced something in the past at least once.",
            explanationId = "Digunakan untuk menyatakan bahwa seseorang pernah mengalami atau melakukan sesuatu di masa lalu.",
            nuanceNotes = "Jika V-Jisho + ことがある artinya menjadi 'kadang-kadang melakukan'.",
            cautionNotes = "Bedakan V-Ta (pengalaman) dengan V-Jisho (frekuensi/kebiasaan).",
            examples = listOf(
                ExampleSentence("富士山に登ったことがありますか。", "ふじさんにのぼったことがありますか。", "Have you ever climbed Mount Fuji?", "Apakah kamu pernah mendaki Gunung Fuji?"),
                ExampleSentence("納豆を食べたことがあります。", "なっとうをたべたことがあります。", "I have eaten natto before.", "Saya pernah makan natto.")
            ),
            relatedGrammar = listOf("〜ことがある (kebiasaan)"),
            tags = listOf("N4", "Minna no Nihongo", "Pengalaman")
        ),
        GrammarPoint(
            id = "n4-g-02",
            bunpouNumber = 2,
            chapterNumber = 2,
            chapterName = "Saran & Anjuran",
            title = "〜ほうがいい",
            meaningEn = "Had better / Should...",
            meaningId = "Sebaiknya / Lebih baik...",
            level = JLPTLevel.N4,
            category = "Saran",
            structure = listOf("V (Ta-form) + ほうがいい (anjuran)", "V (Nai-form) + ほうがいい (larangan halus)"),
            explanationEn = "Used to give strong advice or suggestions to the listener.",
            explanationId = "Digunakan untuk memberikan saran atau anjuran tindakan yang sebaiknya diambil.",
            nuanceNotes = "Cukup persuasif dan tegas.",
            cautionNotes = "Untuk anjuran positif gunakan bentuk -ta; untuk larangan gunakan bentuk -nai.",
            examples = listOf(
                ExampleSentence("風邪を引いたなら、早く寝たほうがいいよ。", "かぜをひいたなら、はやくねたほうがいいよ。", "If you caught a cold, you had better go to bed early.", "Kalau kamu masuk angin, sebaiknya tidur lebih awal."),
                ExampleSentence("甘いものを食べすぎないほうがいい。", "あまいものをたべすぎないほうがいい。", "You should not eat too many sweets.", "Sebaiknya jangan terlalu banyak makan makanan manis.")
            ),
            relatedGrammar = listOf("〜たらどうですか"),
            tags = listOf("N4", "Minna no Nihongo", "Saran")
        ),
        // N5 Grammar Points
        GrammarPoint(
            id = "n5-g-01",
            bunpouNumber = 1,
            chapterNumber = 1,
            chapterName = "Partikel Dasar",
            title = "Partikel は (Wa) & が (Ga)",
            meaningEn = "Topic Marker (Wa) & Subject Identifier (Ga)",
            meaningId = "Partikel Topik (Wa) & Subjek Penegas (Ga)",
            level = JLPTLevel.N5,
            category = "Partikel",
            structure = listOf("N + は", "N + が"),
            explanationEn = "は sets the broad topic of conversation ('As for...'). が identifies the specific actor/subject or introduces new information.",
            explanationId = "は menandai topik utama pembicaraan ('Mengenai...'). が menandai subjek pelaku spesifik atau informasi baru yang ditegaskan.",
            nuanceNotes = "A は B です = Sebagai topik A, adalah B.",
            cautionNotes = "Huruf は dibaca 'wa' saat berfungsi sebagai partikel.",
            examples = listOf(
                ExampleSentence("私は学生です。", "わたしはがくせいです。", "I am a student.", "Saya adalah mahasiswa/pelajar."),
                ExampleSentence("雨が降っています。", "あめがふっています。", "It is raining (rain is falling).", "Hujan sedang turun.")
            ),
            relatedGrammar = listOf("Partikel を", "Partikel に"),
            tags = listOf("N5", "Minna no Nihongo", "Partikel")
        ),
        // N2 Grammar Points
        GrammarPoint(
            id = "n2-g-01",
            bunpouNumber = 1,
            chapterNumber = 1,
            chapterName = "Waktu & Kecepatan",
            title = "〜次第 (Shidai)",
            meaningEn = "As soon as / Depending on",
            meaningId = "Segera setelah / Bergantung pada",
            level = JLPTLevel.N2,
            category = "Waktu & Kondisi",
            structure = listOf("V (Masu-stem) + 次第 (segera setelah)", "N + 次第 (bergantung pada)"),
            explanationEn = "1. When attached to verb-stem: 'As soon as X happens, I will do Y'. 2. When attached to Noun: 'Depends on X'.",
            explanationId = "1. Tempel pada kata kerja bentuk masu (tanpa masu): Segera setelah X selesai, akan langsung melakukan Y. 2. Tempel pada kata benda: Semua tergantung pada X.",
            nuanceNotes = "Sangat sering digunakan dalam email bisnis (keigo).",
            cautionNotes = "Bagian setelahnya biasanya merupakan tindakan berkehendak dari pembicara.",
            examples = listOf(
                ExampleSentence("詳しい日程が決まり次第、ご連絡いたします。", "くわしいにっていがきまりしだい、ごれんらくいたします。", "As soon as the detailed schedule is decided, I will contact you.", "Segera setelah jadwal rinci diputuskan, saya akan menghubungi Anda."),
                ExampleSentence("合格できるかどうかは、あなたの努力次第です。", "ごうかくできるかどうかは、あなたのごどりょくしだいです。", "Whether you can pass or not depends on your efforts.", "Bisa lulus atau tidaknya semua bergantung pada usaha Anda.")
            ),
            relatedGrammar = listOf("〜たとたん", "〜やいなや"),
            tags = listOf("N2", "Kanzen Master N2", "Waktu", "Bisnis")
        )
    )

    // --- VOCABULARY DATA ---
    val VOCAB_LIST: List<VocabWord> = listOf(
        // N3 Vocab
        VocabWord(
            id = "v-n3-01",
            word = "諦める",
            reading = "あきらめる",
            romaji = "akirameru",
            meaningEn = "to give up, to abandon",
            meaningId = "menyerah, putus asa",
            level = JLPTLevel.N3,
            partOfSpeech = "Ichidan Verb (Ichidan)",
            category = "Verbs (Kata Kerja)",
            pitchAccent = "あきら・める [4]",
            example = ExampleSentence("夢を絶対に諦めない。", "ゆめをぜったいにあきらめない。", "Never give up on your dreams.", "Jangan pernah menyerah pada mimpimu."),
            antonyms = listOf("続ける (tsuzukeru)"),
            synonyms = listOf("断念する (dannen suru)")
        ),
        VocabWord(
            id = "v-n3-02",
            word = "遠慮",
            reading = "えんりょ",
            romaji = "enryo",
            meaningEn = "hesitation, restraint, holding back",
            meaningId = "sungkan, menahan diri, ragu-ragu",
            level = JLPTLevel.N3,
            partOfSpeech = "Noun / Suru Verb",
            category = "Nouns (Kata Benda)",
            pitchAccent = "えんりょ [0]",
            example = ExampleSentence("遠慮しないで何でも聞いてください。", "えんりょしないでなんでもきいてください。", "Please don't hesitate to ask anything.", "Jangan sungkan untuk bertanya apa saja."),
            antonyms = listOf("図々しい (zuuzuushii)")
        ),
        VocabWord(
            id = "v-n3-03",
            word = "相変わらず",
            reading = "あいかわらず",
            romaji = "aikawarazu",
            meaningEn = "as ever, as usual, the same as always",
            meaningId = "seperti biasa, tidak berubah dari dulu",
            level = JLPTLevel.N3,
            partOfSpeech = "Adverb (Fukushi)",
            category = "Adverbs (Kata Keterangan)",
            pitchAccent = "あいかわらず [0]",
            example = ExampleSentence("彼は相変わらず元気そうだ。", "かれはあいかわらずげんきそうだ。", "He seems energetic as always.", "Dia kelihatannya sehat dan berenergi seperti biasanya.")
        ),
        VocabWord(
            id = "v-n3-04",
            word = "手続き",
            reading = "てつづき",
            romaji = "tetsuzuki",
            meaningEn = "procedure, formality, paperwork",
            meaningId = "prosedur, proses birokrasi, administrasi",
            level = JLPTLevel.N3,
            partOfSpeech = "Noun / Suru Verb",
            category = "Nouns (Kata Benda)",
            pitchAccent = "てつづき [2]",
            example = ExampleSentence("ビザの更新手続きを済ませた。", "びざのこうしんてつづきをすませた。", "I completed the visa renewal procedure.", "Saya sudah menyelesaikan prosedur perpanjangan visa.")
        ),
        VocabWord(
            id = "v-n3-05",
            word = "もったいない",
            reading = "もったいない",
            romaji = "mottainai",
            meaningEn = "wasteful, too good for",
            meaningId = "sayang (dibuang/disia-siakan), mubazir",
            level = JLPTLevel.N3,
            partOfSpeech = "I-Adjective",
            category = "I-Adjectives (Kata Sifat -i)",
            pitchAccent = "もったいな・い [5]",
            example = ExampleSentence("まだ食べられるのに捨てるのはもったいない。", "まだたべられるのにすてるのはもったいない。", "It is wasteful to throw it away when it is still edible.", "Mubazir membuangnya padahal masih bisa dimakan.")
        ),
        // N4 Vocab
        VocabWord(
            id = "v-n4-01",
            word = "案内する",
            reading = "あんないする",
            romaji = "annai suru",
            meaningEn = "to guide, to show around",
            meaningId = "memandu, mengantar berkeliling, mengarahkan",
            level = JLPTLevel.N4,
            partOfSpeech = "Suru Verb",
            category = "Verbs (Kata Kerja)",
            pitchAccent = "あんない [3]",
            example = ExampleSentence("東京の街を案内します。", "とうきょうのまちをあんないします。", "I will guide you around Tokyo.", "Saya akan memandu Anda berkeliling kota Tokyo.")
        ),
        VocabWord(
            id = "v-n4-02",
            word = "準備",
            reading = "じゅんび",
            romaji = "junbi",
            meaningEn = "preparation, arrangements",
            meaningId = "persiapan",
            level = JLPTLevel.N4,
            partOfSpeech = "Noun / Suru Verb",
            category = "Nouns (Kata Benda)",
            pitchAccent = "じゅ・んび [1]",
            example = ExampleSentence("旅行の準備ができました。", "りょこうのじゅんびができました。", "The preparations for the trip are ready.", "Persiapan untuk liburan sudah selesai.")
        ),
        // N5 Vocab
        VocabWord(
            id = "v-n5-01",
            word = "食べる",
            reading = "たべる",
            romaji = "taberu",
            meaningEn = "to eat",
            meaningId = "makan",
            level = JLPTLevel.N5,
            partOfSpeech = "Ichidan Verb",
            category = "Verbs (Kata Kerja)",
            pitchAccent = "たべ・る [2]",
            example = ExampleSentence("朝ご飯を食べます。", "あさごはんをたべます。", "I eat breakfast.", "Saya sarapan pagi.")
        ),
        // N2 Vocab
        VocabWord(
            id = "v-n2-01",
            word = "把握する",
            reading = "はあくする",
            romaji = "haaku suru",
            meaningEn = "to grasp, to comprehend fully, to understand",
            meaningId = "memahami secara mendalam, menguasai situasi",
            level = JLPTLevel.N2,
            partOfSpeech = "Suru Verb",
            category = "Verbs (Kata Kerja)",
            pitchAccent = "はあく [0]",
            example = ExampleSentence("現状を正確に把握する必要がある。", "げんじょうをせいかくにはあくするひつようがある。", "It is necessary to accurately grasp the current situation.", "Perlu memahami situasi terkini secara akurat.")
        )
    )

    // --- KANJI DATA ---
    val KANJI_LIST: List<KanjiItem> = listOf(
        KanjiItem(
            id = "k-n3-01",
            kanji = "諦",
            onyomi = listOf("テイ", "タイ"),
            kunyomi = listOf("あきら.める", "つまび.らか"),
            meaningEn = "give up, abandon, clarify",
            meaningId = "menyerah, putus asa, jelas",
            level = JLPTLevel.N3,
            strokes = 16,
            radical = "言 (kata/bicara)",
            radicalMeaning = "speech radical",
            grade = 8,
            jukugo = listOf(
                KanjiJukugo("諦念", "ていねん", "resignation, acceptance", "sikap pasrah / menerima takdir"),
                KanjiJukugo("諦観", "ていかん", "clear contemplation", "pandangan jernih berwawasan luas")
            ),
            exampleSentence = ExampleSentence("最後まで諦めずに頑張ろう。", "さいごまであきらめずにはんばろう。", "Let's do our best without giving up until the very end.", "Mari berjuang tanpa menyerah hingga akhir.")
        ),
        KanjiItem(
            id = "k-n3-02",
            kanji = "情",
            onyomi = listOf("ジョウ", "セイ"),
            kunyomi = listOf("なさ.け"),
            meaningEn = "feelings, emotion, passion, condition",
            meaningId = "perasaan, emosi, kasih sayang, keadaan",
            level = JLPTLevel.N3,
            strokes = 11,
            radical = "忄 / 心 (hati)",
            radicalMeaning = "heart radical",
            grade = 5,
            jukugo = listOf(
                KanjiJukugo("情報", "じょうほう", "information, data", "informasi, berita"),
                KanjiJukugo("感情", "かんじょう", "emotion, feelings", "emosi, perasaan hati"),
                KanjiJukugo("事情", "じじょう", "circumstances, reasons", "keadaan, latar belakang peristiwa")
            ),
            exampleSentence = ExampleSentence("最新の情報をインターネットで調べる。", "さいしんのじょうほうをいんたーねっとでしらべる。", "Look up the latest information on the internet.", "Mencari informasi terbaru di internet.")
        ),
        KanjiItem(
            id = "k-n3-03",
            kanji = "試",
            onyomi = listOf("シ"),
            kunyomi = listOf("こころ.みる", "ため.す"),
            meaningEn = "test, try, attempt, experiment",
            meaningId = "ujian, mencoba, mengetes",
            level = JLPTLevel.N3,
            strokes = 13,
            radical = "言 (kata)",
            radicalMeaning = "speech radical",
            grade = 4,
            jukugo = listOf(
                KanjiJukugo("試験", "しけん", "exam, test", "ujian, tes"),
                KanjiJukugo("試合", "しあい", "match, game, contest", "pertandingan olahraga"),
                KanjiJukugo("試着", "しちゃく", "trying on clothes", "mencoba pakaian (fitting)")
            ),
            exampleSentence = ExampleSentence("明日は日本語の試験がある。", "あしたはにほんごのしけんがある。", "Tomorrow there is a Japanese language exam.", "Besok ada ujian bahasa Jepang.")
        ),
        KanjiItem(
            id = "k-n4-01",
            kanji = "勉",
            onyomi = listOf("ベン"),
            kunyomi = listOf("つと.める"),
            meaningEn = "exertion, strive, study",
            meaningId = "berusaha keras, rajin, belajar",
            level = JLPTLevel.N4,
            strokes = 10,
            radical = "力 (tenaga)",
            radicalMeaning = "power / strength radical",
            grade = 3,
            jukugo = listOf(
                KanjiJukugo("勉強", "べんきょう", "study, diligence", "belajar")
            ),
            exampleSentence = ExampleSentence("毎日日本語を勉強しています。", "まいにちにほんごをべんきょうしています。", "I study Japanese every day.", "Saya belajar bahasa Jepang setiap hari.")
        ),
        KanjiItem(
            id = "k-n5-01",
            kanji = "日",
            onyomi = listOf("ニチ", "ジツ"),
            kunyomi = listOf("ひ", "か"),
            meaningEn = "day, sun, Japan",
            meaningId = "hari, matahari, Jepang",
            level = JLPTLevel.N5,
            strokes = 4,
            radical = "日 (matahari)",
            radicalMeaning = "sun radical",
            grade = 1,
            jukugo = listOf(
                KanjiJukugo("日本", "にほん / にっぽん", "Japan", "Jepang"),
                KanjiJukugo("日曜日", "にちようび", "Sunday", "Hari Minggu")
            ),
            exampleSentence = ExampleSentence("今日はいい天気ですね。", "きょうはいいてんきですね。", "It's nice weather today, isn't it?", "Hari ini cuacanya bagus ya.")
        )
    )

    // --- INITIAL SRS DECK ---
    fun getInitialSRSDeck(): List<SRSItem> {
        return listOf(
            SRSItem(
                id = "srs-01",
                cardType = "kanji",
                targetId = "k-n3-01",
                frontText = "諦",
                frontSubText = "16 Goresan • Radikal: 言",
                backReading = "あきら・める (テイ)",
                backMeaningEn = "Give up, abandon",
                backMeaningId = "Menyerah, putus asa",
                exampleJapanese = "夢を諦めないでください。",
                exampleReading = "ゆめをあきらめないでください。",
                exampleTranslation = "Jangan menyerah menggapai mimpimu.",
                level = JLPTLevel.N3
            ),
            SRSItem(
                id = "srs-02",
                cardType = "vocab",
                targetId = "v-n3-02",
                frontText = "遠慮",
                frontSubText = "Noun / Suru Verb",
                backReading = "えんりょ (enryo)",
                backMeaningEn = "Hesitation, holding back, restraint",
                backMeaningId = "Sungkan, ragu-ragu, menahan diri",
                exampleJapanese = "遠慮しないで食べてね。",
                exampleReading = "えんりょしないでたべてね。",
                exampleTranslation = "Makanlah, jangan sungkan ya.",
                level = JLPTLevel.N3
            ),
            SRSItem(
                id = "srs-03",
                cardType = "grammar",
                targetId = "n3-g-01",
                frontText = "〜せいで / 〜せいか",
                frontSubText = "Alasan & Penyebab (Negatif)",
                backReading = "せいで (seide)",
                backMeaningEn = "Because of / Due to (negative blame)",
                backMeaningId = "Gara-gara (menyalahkan alasan buruk)",
                exampleJapanese = "雨のせいで試合が中止になった。",
                exampleReading = "あめのせいでしあいがちゅうしになった。",
                exampleTranslation = "Gara-gara hujan, pertandingan dibatalkan.",
                level = JLPTLevel.N3
            ),
            SRSItem(
                id = "srs-04",
                cardType = "grammar",
                targetId = "n3-g-03",
                frontText = "〜わけにはいかない",
                frontSubText = "Kewajiban Etika & Moral",
                backReading = "わけにはいかない",
                backMeaningEn = "Cannot afford to do (due to moral duty)",
                backMeaningId = "Tidak bisa/tidak boleh (karena tanggung jawab moral)",
                exampleJapanese = "約束したから、断るわけにはいかない。",
                exampleReading = "やくそくしたから、ことわるわけにはいかない。",
                exampleTranslation = "Karena sudah berjanji, saya tidak bisa menolaknya.",
                level = JLPTLevel.N3
            )
        )
    }

    // --- EXAM PAPERS ---
    val EXAM_PAPERS: List<ExamPaper> = listOf(
        ExamPaper(
            id = "exam-n3-2024-12",
            title = "JLPT N3 Official Simulation 2024 (Desember)",
            titleJp = "JLPT N3 2024年12月 実践模擬試験",
            level = JLPTLevel.N3,
            year = "2024",
            session = "December",
            description = "Simulasi lengkap ujian resmi JLPT N3 dengan format waktu standar: Kosakata (Goi), Tata Bahasa & Bacaan (Bunpou & Dokkai), serta Soal Bintang (★ Star Question).",
            totalTimeMinutes = 105,
            totalMaxScore = 180,
            passingScore = 95,
            sections = listOf(
                ExamSectionConfig(
                    type = "vocabulary",
                    title = "Kosakata & Kanji (Goi)",
                    titleJp = "言語知識（文字・語彙）",
                    timeMinutes = 30,
                    maxScore = 60,
                    minPassScore = 19,
                    questions = listOf(
                        ExamQuestion(
                            id = "q1",
                            level = JLPTLevel.N3,
                            section = "vocabulary",
                            subCategory = "Kanji Reading",
                            instruction = "＿＿＿の言葉の読み方として最もよいものを、１・２・３・４から一つ選びなさい。",
                            instructionId = "Pilihlah cara baca kanji yang bergaris bawah berikut.",
                            question = "この計画は非常に<u>重要</u>です。",
                            questionRuby = "この けいかくは ひじょうに <u>じゅうよう</u> です。",
                            options = listOf("じゅうよう", "ちょうよう", "じゅうようう", "じゅうゆ"),
                            correctAnswer = 0,
                            explanationEn = "重要 is read as じゅうよう (juuyou), meaning 'important' or 'crucial'.",
                            explanationId = "Kata 重要 dibaca じゅうよう (juuyou), yang artinya 'sangat penting'."
                        ),
                        ExamQuestion(
                            id = "q2",
                            level = JLPTLevel.N3,
                            section = "vocabulary",
                            subCategory = "Contextual Usage",
                            instruction = "（　）に入れるのに最もよいものを、１・２・３・４から一つ選びなさい。",
                            instructionId = "Pilihlah kata yang paling tepat untuk mengisi bagian dalam kurung.",
                            question = "熱が下がったので、（　）元気になりました。",
                            options = listOf("だいぶ", "まるで", "けっして", "めったに"),
                            correctAnswer = 0,
                            explanationEn = "だいぶ (daibu) means 'considerably' or 'much better'.",
                            explanationId = "だいぶ (daibu) berarti 'sudah lumayan/jauh lebih baik'."
                        )
                    )
                ),
                ExamSectionConfig(
                    type = "grammar",
                    title = "Tata Bahasa & Soal Bintang (Bunpou)",
                    titleJp = "言語知識（文法）",
                    timeMinutes = 35,
                    maxScore = 60,
                    minPassScore = 19,
                    questions = listOf(
                        ExamQuestion(
                            id = "q3",
                            level = JLPTLevel.N3,
                            section = "grammar",
                            subCategory = "Grammar Form",
                            instruction = "次の文の（　）に入れるのに最もよいものを一つ選びなさい。",
                            instructionId = "Pilihlah bentuk tata bahasa yang tepat untuk mengisi kalimat di bawah.",
                            question = "先生の熱心なご指導の（　）、試験に合格することができました。",
                            options = listOf("おかげで", "せいで", "かわりに", "とおりに"),
                            correctAnswer = 0,
                            explanationEn = "〜おかげで indicates a positive outcome thanks to someone's help.",
                            explanationId = "〜おかげで (berkat) digunakan untuk hasil positif yang disyukuri atas bimbingan guru."
                        ),
                        ExamQuestion(
                            id = "q4",
                            level = JLPTLevel.N3,
                            section = "grammar",
                            subCategory = "Star Sentence",
                            instruction = "次の文の ★ に入る最もよいものを、１・２・３・４から一つ選びなさい。",
                            instructionId = "Susunlah 4 pilihan berikut menjadi kalimat yang benar, lalu pilih kata yang berada di posisi bintang (★).",
                            question = "健康のために、＿＿ ＿＿ ＿★＿ ＿＿ ほうがいいですよ。",
                            options = listOf("夜遅く", "食べない", "甘いものを", "には"),
                            correctAnswer = 2,
                            starData = StarQuestionData(
                                preText = "健康のために、",
                                postText = "ほうがいいですよ。",
                                starSlotIndex = 2,
                                correctOrder = listOf(0, 3, 2, 1), // 夜遅く(0) には(3) 甘いものを(2) 食べない(1)
                                options = listOf("夜遅く", "食べない", "甘いものを", "には")
                            ),
                            explanationEn = "Correct sentence order: 夜遅く(1) には(4) 甘いものを(3) 食べない(2) ほうがいいですよ. At the ★ position (3rd slot) is 甘いものを.",
                            explanationId = "Urutan yang benar: 夜遅く には 甘いものを 食べない ほうがいいですよ. Pilihan pada posisi ★ (slot ke-3) adalah 甘いものを (Opsi 3)."
                        )
                    )
                ),
                ExamSectionConfig(
                    type = "reading",
                    title = "Pemahaman Bacaan (Dokkai)",
                    titleJp = "読解 (Dokkai)",
                    timeMinutes = 40,
                    maxScore = 60,
                    minPassScore = 19,
                    questions = listOf(
                        ExamQuestion(
                            id = "q5",
                            level = JLPTLevel.N3,
                            section = "reading",
                            subCategory = "Short Passage",
                            instruction = "次の文章を読んで、後の問いに対する答えとして最もよいものを一つ選びなさい。",
                            instructionId = "Bacalah teks berikut dan jawablah pertanyaan berdasarkan isi teks.",
                            readingPassage = "最近、オンラインで外国語を学ぶ人が増えています。教室に通う時間が省けるだけでなく、世界中のネイティブ講師と好きな時間にレッスンができるからです。しかし、自分でスケジュールを管理する強い意志がないと、途中でやめてしまう人も少なくありません。",
                            passageTitle = "オンライン学習のメリットと課題",
                            passageData = DokkaiPassageData(
                                id = "dp-01",
                                title = "オンライン学習のメリットと課題",
                                passage = "最近、オンラインで外国語を学ぶ人が増えています。教室に通う時間が省けるだけでなく、世界中のネイティブ講師と好きな時間にレッスンができるからです。しかし、自分でスケジュールを管理する強い意志がないと、途中でやめてしまう人も少なくありません。",
                                passageType = "short"
                            ),
                            question = "文章の内容と合っているものはどれですか。",
                            options = listOf(
                                "オンライン学習は時間を節約できるが、自己管理が必要である。",
                                "教室に通うほうが必ずオンラインより長続きする。",
                                "ネイティブ講師のレッスンは決まった時間にしか受けられない。",
                                "オンラインで学ぶ人は減りつつある。"
                            ),
                            correctAnswer = 0,
                            explanationEn = "The passage mentions that online learning saves commuting time, but requires strong self-discipline to manage one's schedule without quitting.",
                            explanationId = "Teks menyatakan bahwa belajar daring menghemat waktu transportasi, namun membutuhkan disiplin diri agar tidak berhenti di tengah jalan."
                        )
                    )
                )
            )
        ),
        ExamPaper(
            id = "exam-n4-mock-01",
            title = "JLPT N4 Standard Mock Test",
            titleJp = "JLPT N4 模擬試験 (Standard)",
            level = JLPTLevel.N4,
            year = "2024",
            session = "Mock 1",
            description = "Tes simulasi standar JLPT N4 mencakup pola kalimat Minna no Nihongo II bab 26-50 dan kosakata harian.",
            totalTimeMinutes = 85,
            totalMaxScore = 180,
            passingScore = 90,
            sections = listOf(
                ExamSectionConfig(
                    type = "vocabulary",
                    title = "Kosakata & Kanji (Goi)",
                    titleJp = "文字・語彙",
                    timeMinutes = 25,
                    maxScore = 60,
                    minPassScore = 19,
                    questions = listOf(
                        ExamQuestion(
                            id = "q-n4-01",
                            level = JLPTLevel.N4,
                            section = "vocabulary",
                            subCategory = "Kanji Reading",
                            instruction = "下線の漢字の読み方を選んでください。",
                            question = "友達に<u>案内</u>してもらった。",
                            options = listOf("あんない", "かんない", "あんないい", "おくない"),
                            correctAnswer = 0,
                            explanationEn = "案内 is read as あんない (annai), meaning guidance/showing around.",
                            explanationId = "案内 dibaca あんない (annai), artinya memandu."
                        )
                    )
                ),
                ExamSectionConfig(
                    type = "grammar",
                    title = "Tata Bahasa (Bunpou)",
                    titleJp = "文法",
                    timeMinutes = 30,
                    maxScore = 60,
                    minPassScore = 19,
                    questions = listOf(
                        ExamQuestion(
                            id = "q-n4-02",
                            level = JLPTLevel.N4,
                            section = "grammar",
                            subCategory = "Grammar Choice",
                            instruction = "正しい文法を選びなさい。",
                            question = "日本へ行った（　）がありますか。",
                            options = listOf("こと", "もの", "ところ", "わけ"),
                            correctAnswer = 0,
                            explanationEn = "〜たことがある expresses past experience.",
                            explanationId = "〜たことがある menyatakan pengalaman pernah melakukan sesuatu di masa lalu."
                        )
                    )
                )
            )
        )
    )

    // --- YOUTUBE CURATED VIDEOS ---
    val YOUTUBE_VIDEOS: List<YouTubeVideo> = listOf(
        YouTubeVideo(
            id = "yt-01",
            title = "JLPT N3 Grammar in 60 Minutes - Full Kanzen Master Review",
            channel = "Nihongo no Mori",
            duration = "58:40",
            level = JLPTLevel.N3,
            category = "Grammar",
            youtubeId = "dQw4w9WgXcQ",
            description = "Ringkasan super padat tata bahasa N3 paling sering keluar di ujian JLPT bersama sensei native."
        ),
        YouTubeVideo(
            id = "yt-02",
            title = "Cara Cepat Menjawab Soal Bintang (★) JLPT Tanpa Pusing",
            channel = "Nihongo Master Indonesia",
            duration = "18:22",
            level = JLPTLevel.N3,
            category = "Exam Strategy",
            youtubeId = "dQw4w9WgXcQ",
            description = "Trik logika partikel dan pola kalimat untuk selalu dapat poin penuh di sesi Star Question."
        ),
        YouTubeVideo(
            id = "yt-03",
            title = "100 Kosakata Wajib JLPT N4 & Contoh Kalimat Sehari-hari",
            channel = "Japanese Ammo with Miku",
            duration = "24:15",
            level = JLPTLevel.N4,
            category = "Vocabulary",
            youtubeId = "dQw4w9WgXcQ",
            description = "Hafalkan 100 kosakata N4 paling esensial dengan pelafalan intonasi alami."
        ),
        YouTubeVideo(
            id = "yt-04",
            title = "Latihan Pendengaran JLPT N3 Choukai (Listening Practice)",
            channel = "Sambon Juku",
            duration = "32:10",
            level = JLPTLevel.N3,
            category = "Listening",
            youtubeId = "dQw4w9WgXcQ",
            description = "Simulasi soal mendengarkan dengan teks dialog dan analisis jawaban."
        )
    )

    // --- RELEASE NOTES ---
    val RELEASE_NOTES: List<ReleaseNoteItem> = listOf(
        ReleaseNoteItem(
            version = "v2.5.0",
            date = "September 2026",
            title = "NihongoMaster Native Android Edition",
            highlights = listOf(
                "Full Jetpack Compose native architecture with sleek Japanese aesthetic",
                "Spaced Repetition (SRS SM-2) Flashcard system with rich Kanji & Bunpou cards",
                "Timed JLPT Exam Simulation with ★ Star Question builder and Dokkai reader",
                "AI Sensei conversational assistant for Japanese grammar nuances",
                "Support for JLPT N5, N4, N3, and N2 curriculum"
            )
        )
    )
}
