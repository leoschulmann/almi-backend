package com.leoschulmann.almibackend.rest

import com.leoschulmann.almibackend.rest.model.SyncData
import com.leoschulmann.almibackend.rest.model.SyncRequest
import com.leoschulmann.almibackend.rest.model.VerbDto
import com.leoschulmann.almibackend.service.SyncService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sync")
class SyncController(private val syncService: SyncService) {

    @PostMapping("/verb")
    fun syncVerbs(@RequestBody syncRequest: SyncRequest): ResponseEntity<List<VerbDto>> {
        val userData: List<SyncData> = syncRequest.extract()
        val verbUpdates = syncService.getVerbUpdates(userData)
        return ResponseEntity.ok(verbUpdates)
    }
}

