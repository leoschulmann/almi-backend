package com.leoschulmann.almibackend.service

import com.leoschulmann.almibackend.enm.*
import com.leoschulmann.almibackend.enm.GrammaticalPerson.*
import com.leoschulmann.almibackend.enm.VerbForm.*
import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.entity.embeddable.Transliteration
import com.leoschulmann.almibackend.repo.StemRepository
import com.leoschulmann.almibackend.repo.VerbRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

private val possibleForms = setOf(
    Verb(null, null, null, null, INFINITIVE, NONE, Plurality.NONE).getVerbCode(),

    Verb(null, null, null, null, PRESENT, NONE, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PRESENT, NONE, Plurality.PLURAL_FEMININE).getVerbCode(),

    Verb(null, null, null, null, PAST, FIRST, Plurality.SINGULAR).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, FIRST, Plurality.PLURAL).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, PAST, SECOND, Plurality.PLURAL_FEMININE).getVerbCode(),
    Verb(null, null, null, null, PAST, THIRD, Plurality.PLURAL).getVerbCode(),

    Verb(null, null, null, null, FUTURE, FIRST, Plurality.SINGULAR).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, FIRST, Plurality.PLURAL).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, SECOND, Plurality.PLURAL_FEMININE).getVerbCode(),
    Verb(null, null, null, null, FUTURE, THIRD, Plurality.PLURAL_MASCULINE).getVerbCode(),

    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.SINGULAR_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.SINGULAR_FEMININE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.PLURAL_MASCULINE).getVerbCode(),
    Verb(null, null, null, null, IMPERATIVE, NONE, Plurality.PLURAL_FEMININE).getVerbCode()
)

@Service
class ImportService(private val stemRepository: StemRepository, private val verbRepository: VerbRepository) {

    /**
    Processes strings
    שאל,спрашивать;задавать вопрос,ask;question
     */
    fun parseStem(string: String): Stem {

        val tokens = string.split(',').map { l -> l.trim() }.toList()

        val ru = tokens[1].split(';').map { l ->
            Translation(l.trim(), Lang.RU)

        }.toList()
        val en = tokens[2].split(';').map { l ->
            Translation(l.trim(), Lang.EN)
        }.toList()

        return Stem(tokens[0], mutableSetOf(), (ru + en).toMutableList())
    }


    /**
    אמר,NIFAL,נאמרת,נֶאָמַרְתְּ,PAST,SECOND,SINGULAR_FEMININE,неэмарт,ne'emart
     */
    fun parseVerb(line: String): Verb {
        val list: List<String> = line.split(',').map { l -> l.trim() }.toList()

        val stem = stemRepository.findByRegular(list[0])
        if (stem == null) {
            log.warn { "Can't find stem $list[0]" }
        }

        var regular: String = list[2]
        var niqqud: String = list[3]

        if (hasNiqqud(list[2])) {
            niqqud = list[2]
            regular = list[3]
        }

        val verb = Verb(
            regular, niqqud, stem, Binyan.valueOf(list[1]), VerbForm.valueOf(list[4]),
            GrammaticalPerson.valueOf(list[5]), Plurality.valueOf(list[6])
        )

        verb.transliteration.add(Transliteration(list[7], Lang.RU))
        verb.transliteration.add(Transliteration(list[8], Lang.EN))

        return verb
    }

    // Glory to the robots!
    fun hasNiqqud(text: String): Boolean {
        val hebrewLettersRange = '\u0590'..'\u05FF'
        val niqqudChars = setOf(
            '\u05B0', '\u05B1', '\u05B2', '\u05B3', '\u05B4',
            '\u05B5', '\u05B6', '\u05B7', '\u05B8', '\u05B9',
            '\u05BB', '\u05BC', '\u05C1', '\u05C2', '\u05C7'
        )

        for (char in text) {
            if (char !in hebrewLettersRange && char !in niqqudChars) {
                throw IllegalArgumentException("Non hebrew char detected: $char")
            }
            if (char in niqqudChars) {
                return true
            }
        }
        return false
    }

    fun validateListVerbs(verbs: MutableList<Verb>) {
        val testSet = possibleForms.toMutableSet()

        verbs.forEach { verb ->
            val verbCode = verb.getVerbCode()
            testSet.remove(verbCode)
        }

        if (testSet.isEmpty()) {
            return
        }
        val missingVerbs: String = testSet.joinToString(
            transform = fun(code: Int): CharSequence = Verb.decode(code),
            separator = ", "
        )
        throw IllegalArgumentException("Incomplete verb set, missing ${testSet.size} forms [$missingVerbs]")
    }
}