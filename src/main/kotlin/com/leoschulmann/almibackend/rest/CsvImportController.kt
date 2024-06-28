package com.leoschulmann.almibackend.rest

import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.repo.ImportDao
import com.leoschulmann.almibackend.service.ImportService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.StringReader

@RestController
@RequestMapping("/csv")
class CsvImportController(private val service: ImportService, private val dao: ImportDao) {

    @PostMapping("/stem")
    fun importStemsCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            val stems: MutableList<Stem> = reader.lines().map { line -> service.parseStem(line) }.toList()
            dao.importStems(stems)
        }
    }

    @PostMapping("/verb")
    fun importVerbCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            val verbs: MutableList<Verb> = reader.lines().map { line -> service.parseVerb(line) }
                .filter { verb -> verb.stem != null && verb.binyan != null && verb.form != null && verb.person != null && verb.plurality != null }
                .toList()
            dao.importVerbs(verbs)
        }
    }
}