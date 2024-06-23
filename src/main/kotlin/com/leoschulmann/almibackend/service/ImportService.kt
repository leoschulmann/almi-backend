package com.leoschulmann.almibackend.service

import com.leoschulmann.almibackend.enm.*
import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.repo.StemRepository
import com.leoschulmann.almibackend.repo.VerbRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

private val log = KotlinLogging.logger {}

@Service
class ImportService(private val stemRepository: StemRepository, private val verbRepository: VerbRepository) {

    /**
    Processes strings
    שאל,спрашивать;задавать вопрос,ask;question
     */
    fun importStemV2(string: String): Stem {

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
    קור,PAAL,קראנו,קָרָאנוּ,PAST,FIRST,PLURAL
     */
    fun importVerb(line: String): Verb {
        val list: List<String> = line.split(',').map { l -> l.trim() }.toList()

        val stem = stemRepository.findByRegular(list[0])
        if (stem == null) {
            log.warn { "Can't find stem $list[0]" }
        }


        return Verb(
            list[2], list[3], stem, Binyan.valueOf(list[1]), VerbForm.valueOf(list[4]),
            GrammaticalPerson.valueOf(list[5]), Plurality.valueOf(list[6])
        )
    }
}