package com.leoschulmann.almibackend.rest

import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.repo.ImportDao
import com.leoschulmann.almibackend.repo.VerbRepository
import com.leoschulmann.almibackend.service.ParseService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.StringReader

@RestController
@RequestMapping("/csv")
class CsvImportController(
    private val parseService: ParseService, private val dao: ImportDao, private val verbRepository: VerbRepository
) {

    @PostMapping("/stem")
    fun importStemsCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            val stems: MutableList<Stem> = reader.lines().map { line -> parseService.parseStem(line) }.toList()
            dao.persistStems(stems)
        }
    }

    @PostMapping("/verb")
    fun importVerbCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            val verbs: MutableList<Verb> = reader.lines().map { line -> parseService.parseVerb(line) }
                .filter { verb -> verb.stem != null && verb.binyan != null && verb.form != null && verb.person != null && verb.plurality != null }
                .toList()
            dao.persistVerbs(verbs)
        }
    }

    @PostMapping("/verbtranslate")
    fun importVerbTranslateCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            val verbs: List<Verb> = reader.lines().map { line -> parseService.parseVerbTranslation(line) }.toList()
            verbRepository.saveAll(verbs)
        }
    }
}