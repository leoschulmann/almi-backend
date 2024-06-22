package com.leoschulmann.almibackend.rest

import com.leoschulmann.almibackend.service.ImportService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.StringReader

@RestController
@RequestMapping("/csv")
class CsvImportController(private val service: ImportService) {

    @PostMapping("/stem")
    fun importStemsCsv(@RequestBody content: String) {
        BufferedReader(StringReader(content)).use { reader ->
            reader.lines().forEach { line -> service.importStem(line) }
        }
    }
}