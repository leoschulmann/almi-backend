package com.leoschulmann.almibackend.repo;

import com.leoschulmann.almibackend.entity.Verb
import org.springframework.data.jpa.repository.JpaRepository

interface VerbRepository : JpaRepository<Verb, Long> {
}