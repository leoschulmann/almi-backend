package com.leoschulmann.almibackend.service

import com.leoschulmann.almibackend.entity.Verb
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
        val entities = findByIdBatched(ids, 100)
        return entities.map { verb -> Mappers.getMapper(VerbMapper::class.java).toDto(verb) }
    }

    private fun findByIdBatched(ids: List<Long>, batchSize: Int): MutableList<Verb> {
        val res = mutableListOf<Verb>()

        for (i in ids.indices step batchSize) {
            val batch = ids.subList(i, minOf(i + batchSize, ids.size))
            res.addAll(verbRepository.findByIdIn(batch))
        }
        return res
    }
}