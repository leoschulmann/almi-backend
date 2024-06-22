package com.leoschulmann.almibackend.entity

import com.leoschulmann.almibackend.entity.embeddable.Translation
import com.leoschulmann.almibackend.entity.embeddable.Transliteration
import jakarta.persistence.*

@Entity
open class Stem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stem_gen")
    @SequenceGenerator(name = "stem_gen", sequenceName = "stem_seq")
    @Column(name = "id", nullable = false)
    open var id: Long? = null
        protected set

    @Column(name = "regular", nullable = false, length = 64)
    open var regular: String? = null

    @OneToMany(mappedBy = "stem", orphanRemoval = true)
    open var verbs: MutableSet<Verb> = mutableSetOf()

    @Column(name = "transliteration")
    @ElementCollection
    @CollectionTable(name = "stem_transliteration", joinColumns = [JoinColumn(name = "stem_id")])
    @OrderColumn
    open var transliteration: MutableList<Transliteration> = mutableListOf()

    @Column(name = "translation")
    @ElementCollection
    @CollectionTable(name = "stem_translation", joinColumns = [JoinColumn(name = "stem_id")])
    @OrderColumn
    open var translation: MutableList<Translation> = mutableListOf()
}