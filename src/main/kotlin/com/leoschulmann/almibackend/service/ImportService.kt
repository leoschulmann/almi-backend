package com.leoschulmann.almibackend.service

import com.leoschulmann.almibackend.enm.Lang
import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.repo.StemRepository
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class ImportService(private val stemRepository: StemRepository) {

    /**
      Processes strings as
      אכל,"есть, кушать","to eat"
    */

    fun importStem(input: String) {

        val split: List<String> = input.split(Pattern.compile(","), 2)

        val hebrew: String = split[0];

        val code = split[1].trim().trim('\"')

        val ruList: List<Translation> = code.substring(0, code.indexOf('\"'))
            .split(',')
            .map { str -> Translation(str.trim(), Lang.RU) }
            .toList();

        val en: List<Translation> = code.substring(code.lastIndexOf('\"') + 1, code.length)
            .split(',')
            .map { s -> Translation(s.trim(), Lang.EN) }
            .toList()

        stemRepository.save(Stem(hebrew, emptySet<Verb>().toMutableSet(), (ruList + en).toMutableList()))
    }
}