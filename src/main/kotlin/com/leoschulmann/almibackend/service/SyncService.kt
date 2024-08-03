package com.leoschulmann.almibackend.service

import com.leoschulmann.almibackend.entity.VerbMapper
import com.leoschulmann.almibackend.repo.VerbRepository
import com.leoschulmann.almibackend.rest.model.SyncData
import com.leoschulmann.almibackend.rest.model.VerbDto
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class SyncService(private val verbRepository: VerbRepository) {
    fun getVerbUpdates(userData: List<SyncData>): List<VerbDto> {
        val projections: Set<SyncData> = verbRepository.findProjections() // TODO: cache
        val newVerbs: Set<SyncData> = projections - userData.toSet()
        val obsoleteVerbs: List<SyncData> =
            userData.filter { verb -> projections.any { e -> e.id == verb.id && e.version > verb.version } }
        val ids: List<Long> = (newVerbs + obsoleteVerbs).map { verb -> verb.id }
        val entities = verbRepository.findAllById(ids)
        return entities.map { verb -> Mappers.getMapper(VerbMapper::class.java).toDto(verb) }
    }
}