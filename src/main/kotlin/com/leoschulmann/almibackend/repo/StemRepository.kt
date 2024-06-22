package com.leoschulmann.almibackend.repo;

import com.leoschulmann.almibackend.entity.Stem
import org.springframework.data.jpa.repository.JpaRepository

interface StemRepository : JpaRepository<Stem, Long>