package com.leoschulmann.almibackend.repo;

import com.leoschulmann.almibackend.entity.Verb
import com.leoschulmann.almibackend.rest.model.SyncData
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface VerbRepository : JpaRepository<Verb, Long> {
    @Query("select new com.leoschulmann.almibackend.rest.model.SyncData(v.id, v.version) from Verb v")
    fun findProjections(): Set<SyncData>

    @EntityGraph(attributePaths = ["stem", "transliteration", "translation"])
    fun findByIdIn(ids: List<Long>): List<Verb>
}