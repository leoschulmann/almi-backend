package com.leoschulmann.almibackend.repo

import com.leoschulmann.almibackend.enm.Binyan
import com.leoschulmann.almibackend.enm.GrammaticalPerson
import com.leoschulmann.almibackend.enm.Plurality
import com.leoschulmann.almibackend.enm.VerbForm
import com.leoschulmann.almibackend.entity.Stem
import com.leoschulmann.almibackend.entity.Verb
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.validation.Validator
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

private val log = KotlinLogging.logger {}

@Repository
@PersistenceContext
class ImportDao(private val em: EntityManager, private val validator: Validator) {

    @Transactional
    fun importStems(stems: List<Stem>) {
        if (stems.isEmpty()) {
            log.info { "Passed zero stems to persist" }
            return
        }

        var count = 0

        stems.forEach { stem ->
            if (checkStemExistence(stem.regular)) {
                log.warn { "Stem ${stem.regular} already exists!" }
            } else {
                em.persist(stem)
                log.info { "Saved stem: ${stem.regular}" }
                count++
            }
        }
        log.info { "Saved $count stems total" }
    }

    fun checkStemExistence(input: String?): Boolean {
        return em.createQuery("SELECT COUNT(s) FROM Stem s WHERE s.regular = :input", Long::class.java)
            .setParameter("input", input).singleResult > 0
    }

    @Transactional
    fun importVerbs(verbs: List<Verb>) {
        var count = 0

        verbs.forEach { verb: Verb ->
            validator.validate(verb)

            if (checkVerbExistence(verb.stem, verb.binyan, verb.form, verb.person, verb.plurality)) {
                log.warn { "Verb $verb already exists!" }
            } else {
                em.persist(verb)
                log.info { "Saved verb: ${verb.regular}" }
                count++
            }
        }
        log.info { "Saved $count verbs total" }
    }

    private fun checkVerbExistence(
        stem: Stem?, binyan: Binyan?, form: VerbForm?, person: GrammaticalPerson?, plurality: Plurality?
    ): Boolean {
        return em.createQuery(
            """SELECT COUNT(v) FROM Verb v WHERE v.stem = :stem AND v.binyan = :binyan 
                |AND v.form = :form AND v.person = :person AND v.plurality = :pl""".trimMargin(), Long::class.java
        ).setParameter("stem", stem).setParameter("binyan", binyan).setParameter("form", form)
            .setParameter("person", person).setParameter("pl", plurality).singleResult > 0
    }
}